package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.dto.UpdateUserDto;
import com.secondcommit.forum.dto.UserResponseDto;
import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Role;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.RoleRepository;
import com.secondcommit.forum.repositories.SubjectRepository;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.cloudinary.CloudinaryServiceImpl;
import com.secondcommit.forum.services.sparkpost.SparkPostServiceImpl;
import com.sparkpost.exception.SparkPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Implementation of the User Service Interface
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final SparkPostServiceImpl sparkPost;

    @Autowired
    private final CloudinaryServiceImpl cloudinary;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder encoder, SparkPostServiceImpl sparkPost,
                           CloudinaryServiceImpl cloudinary, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.sparkPost = sparkPost;
        this.cloudinary = cloudinary;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Method to create a new user
     * @param newUser (NewUserRequest)
     * @return ResponseEntity (ok: userDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> createUser(NewUserRequest newUser, String roleName) {

        //Access the repository to check if the username and/or email aren't being used yet
        Optional<User> userOpt = userRepository.findByUsername(newUser.getUsername());

        if (userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The username " + newUser.getUsername() + " is already being used" ));

        userOpt = userRepository.findByEmail(newUser.getEmail());

        if (userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The email " + newUser.getEmail() + " is already being used" ));

        //If it gets here, the validations were ok, so we create a new user
        User user = new User();

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER").get());

        if (roleName.equalsIgnoreCase("ADMIN"))
            roles.add(roleRepository.findByName("ADMIN").get());

        //Validates subjects
        Set<Subject> validSubjects = new HashSet<>();

        if (newUser.getHasAccess() != null){
            for (String strSubject : newUser.getHasAccess()){
                Optional<Subject> subjectOpt = subjectRepository.findByName(strSubject);
                if (subjectOpt.isPresent())
                    validSubjects.add(subjectOpt.get());
            }
        }

        user = new User(newUser.getEmail(), newUser.getUsername(),
                encoder.encode(newUser.getPassword()), roles, validSubjects);

        //Saves image in Cloudinary
        if (newUser.getAvatar() != null){
            try {
                File photo = cloudinary.uploadImage(newUser.getAvatar());
                user.setAvatar(photo);
            } catch (Exception e){
                System.err.println("Error: " + e.getMessage());
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Upload failed"));
            }
        }

        //Saves the user in the database
        userRepository.save(user);

        //Sends an email with the validation code;
        try {
            sparkPost.sendActivationMessage(user);
        } catch (SparkPostException e){
            System.err.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok().body(user.getDtoFromUser("Check your email account"));
    }

    /**
     * Method to validate a new user
     * @param user
     * @param activationCode int
     * @return ResponseEntity (ok: messageResponse, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> activateUser(User user, Integer activationCode) {

        if (user.getActivationCode() != null && activationCode.intValue() == user.getActivationCode().intValue()) {
            user.setIsActivated(true);
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The activation code is wrong"));
        }

        //Sends an email with a welcome message
        try {
            sparkPost.sendWelcomeMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .body(new MessageResponse("Your account has been activated with success"));
    }

    /**
     * Method to update an avatar. Only one per user is allowed
     * @param username (Gets from the jwt token)
     * @param avatar (MultipartFile)
     * @return ResponseEntity (ok: url, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addAvatar(String username, MultipartFile avatar) {

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Checks if the user already has a file. If yes, destroys it
        if (userOpt.get().getAvatar() != null){

            try {
                Boolean destroyed = cloudinary.deleteFile(userOpt.get().getAvatar().getCloudinaryId());
                if (destroyed) userOpt.get().setAvatar(null);

            } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
            }
        }

        //Saves image in Cloudinary
        try {
            File photo = cloudinary.uploadImage(avatar);
            userOpt.get().setAvatar(photo);
            userRepository.save(userOpt.get());
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Upload failed"));
        }

        return ResponseEntity.ok()
                .body(new MessageResponse(userOpt.get().getAvatar().getUrl()));
    }

    /**
     * Method to get user
     * @param id
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getUser(Long id) {

        //Gets User
        Optional<User> userOpt = userRepository.findById(id);

        return ResponseEntity.ok(userOpt.get().getDtoFromUser(" "));
    }

    /**
     * Method to get all users
     * @return user
     */
    @Override
    public ResponseEntity<?> getAllUsers() {

        //Gets all users
        List<User> users = userRepository.findAll();
        List<UserResponseDto> response = new ArrayList<UserResponseDto>();

        for (User user : users) response.add(user.getDtoFromUser(" "));

        return ResponseEntity.ok(response);
    }

    /**
     * Method to update User (only username, email, isActivated, hasAccess(Set<String>)
     * Sends an alert email after the update
     * @param id
     * @param userDto
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto, String username) {

        //Gets User
        Optional<User> userOpt = userRepository.findById(id);
        Optional<User> userConnecting = userRepository.findByUsername(username);

        //Tests if the user is allowed to edit this post (only authors and admins can do it)
        //If the user isn't the one trying to update, checks to see if the user is ADMIN
        if (!userConnecting.get().getUsername().equalsIgnoreCase(username)){

            boolean isAdmin = false;

            for (Role role  : userOpt.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to update the post " ));
        }

        //Validates username
        if (userOpt.get().getUsername() != userDto.getUsername() &&
                userRepository.existsByUsername(userDto.getUsername()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This username is already being used" ));

        //Validates email
        if (userOpt.get().getEmail() != userDto.getEmail() &&
                userRepository.existsByEmail(userDto.getEmail()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This email is already being used" ));

        //Starts updating
        userOpt.get().setUsername(userDto.getUsername());
        userOpt.get().setEmail(userDto.getEmail());

        if (userDto.getActivated() != null)
            userOpt.get().setIsActivated(userDto.getActivated());

        //Validates subjects
        Set<Subject> validSubjects = new HashSet<>();

        if (userDto.getHasAccess() != null){
            for (String strSubject : userDto.getHasAccess()){
                Optional<Subject> subjectOpt = subjectRepository.findByName(strSubject);
                if (subjectOpt.isPresent())
                    validSubjects.add(subjectOpt.get());
            }

            userOpt.get().setHasAccess(validSubjects);
        }

        userRepository.save(userOpt.get());

        //Sends an email to the user
        try {
            sparkPost.sendUserUpdatedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok(userOpt.get().getDtoFromUser("Your account has been updated"));
    }

    /**
     * Method to elete the user. Sends a goodbye email
     * @param id
     * @return ResponseEntity (ok: messageResponse, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> deleteUser(Long id, String username) {

        //Gets the user
        Optional<User> userOpt = userRepository.findById(id);
        Optional<User> userConnecting = userRepository.findByUsername(username);

        //Tests if the user is allowed to edit this post (only authors and admins can do it)
        //If the user isn't the one trying to delete, checks to see if the user is ADMIN
        if (!userOpt.get().getUsername().equalsIgnoreCase(username)){

            boolean isAdmin = false;

            for (Role role  : userConnecting.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to delete the user " +
                                userOpt.get().getUsername()));
        }

        //Checks if the user already has a file. If yes, destroys it
        if (userOpt.get().getAvatar() != null){

            try {
                Boolean destroyed = cloudinary.deleteFile(userOpt.get().getAvatar().getCloudinaryId());
                if (destroyed) userOpt.get().setAvatar(null);

            } catch (IOException e){
                System.err.println("Error: " + e.getMessage());
            }
        }

        userRepository.delete(userOpt.get());

        //Sends an email to the user
        try {
            sparkPost.sendUserRemovedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok().body(new MessageResponse("User " + id + " deleted with success"));
    }

    /**
     * Method to add access to one subject
     * @param id
     * @param subjectDto
     * @return ResponseEntity (ok: userDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addAccess(Long id, SubjectDto subjectDto) {

        //Gets User
        Optional<User> userOpt = userRepository.findById(id);

        //Validates Subject
        Optional<Subject> subjectOpt = subjectRepository.findByName(subjectDto.getName());

        if (subjectOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid subject"));

        userOpt.get().addAccess(subjectOpt.get());
        userRepository.save(userOpt.get());

        return ResponseEntity.ok(userOpt.get().getDtoFromUser("Added access to the subject " + subjectDto.getName()));
    }

    /**
     * Method to remove access to a subject
     * @param id
     * @param subjectDto
     * @return ResponseEntity (ok: userDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> removeAccess(Long id, SubjectDto subjectDto) {

        //Gets User
        Optional<User> userOpt = userRepository.findById(id);

        //Validates Subject
        Optional<Subject> subjectOpt = subjectRepository.findByName(subjectDto.getName());

        if (subjectOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid subject"));

        userOpt.get().removeAccess(subjectOpt.get());
        userRepository.save(userOpt.get());

        return ResponseEntity.ok(userOpt.get().getDtoFromUser("Removed access from subject " + subjectDto.getName()));
    }

}
