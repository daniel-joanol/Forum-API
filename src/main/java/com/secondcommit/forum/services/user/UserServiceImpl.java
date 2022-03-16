package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.dto.UpdateUserDto;
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
     * Method that creates a new user
     * @param newUser
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> createUser(NewUserRequest newUser) {

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

        List<Role> rolesRepo = roleRepository.findAll();
        Optional<Role> role = roleRepository.findByName("USER");
        roles.add(role.get());

        //Creates user without hasAccess(subject)
        if (newUser.getHasAccess() == null){
            user = new User(newUser.getEmail(), newUser.getUsername(),
                    encoder.encode(newUser.getPassword()), roles);
        }

        //Creates user with hasAccess(subject)
        if (newUser.getHasAccess() != null) {

            Set<Subject> validSubjects = new HashSet<>();
            for (Subject subject : newUser.getHasAccess()) {
                if (subjectRepository.existsByName(subject.getName()))
                    validSubjects.add(subject);
            }

            user = new User(newUser.getEmail(), newUser.getUsername(),
                    encoder.encode(newUser.getPassword()), roles, newUser.getHasAccess());
        }

        //Saves the user in the database
        userRepository.save(user);

        //Sends an email with the validation code;
        try {
            sparkPost.sendActivationMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok().body(user.getDtoFromUser());
    }

    /**
     * Validates the new user
     * @param user
     * @param activationCode int
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> activateUser(User user, Integer activationCode) {

        if (activationCode.intValue() == user.getActivationCode().intValue()) {
            user.setActivated(true);
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
     * @param username
     * @param avatar (file)
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> addAvatar(String username, MultipartFile avatar) {

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The username " +  username + " doesn't exist"));

        try {
            File photo = new File(cloudinary.uploadImage(avatar));
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
     * Gets all info from the user
     * @param id
     * @return User
     */
    @Override
    public ResponseEntity<?> getUser(Long id) {

        //Validates User
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return ResponseEntity.ok(userOpt.get());
    }

    /**
     * Updates User (only username, email and isActivated). Sends an alert email
     * @param id
     * @param userDto
     * @return User
     */
    @Override
    public ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto) {

        //Validates User
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        userOpt.get().setUsername(userDto.getUsername());
        userOpt.get().setEmail(userDto.getEmail());

        if (userDto.getActivated() != null)
            userOpt.get().setActivated(userDto.getActivated());

        userRepository.save(userOpt.get());

        try {
            sparkPost.sendUserUpdatedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok(userOpt.get().getDtoFromUser());
    }

    /**
     * Deletes the user. Sends a goodbye email
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<?> deleteUser(Long id) {

        //Validates User
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        userRepository.delete(userOpt.get());

        try {
            sparkPost.sendUserRemovedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok().body(new MessageResponse("User " + id + " deleted with success"));
    }
}
