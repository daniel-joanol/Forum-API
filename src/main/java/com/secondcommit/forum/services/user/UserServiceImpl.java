package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.entities.Role;
import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.RoleRepository;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.sparkpost.SparkPostServiceImpl;
import com.sparkpost.exception.SparkPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final PasswordEncoder encoder;

    @Autowired
    private final SparkPostServiceImpl sparkPost;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder encoder, SparkPostServiceImpl sparkPost) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.sparkPost = sparkPost;
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
            ResponseEntity.badRequest()
                    .body(new MessageResponse("The username " + newUser.getUsername() + " is already being used" ));

        userOpt = userRepository.findByEmail(newUser.getEmail());

        if (userOpt.isPresent())
            ResponseEntity.badRequest()
                    .body(new MessageResponse("The email " + newUser.getEmail() + " is already being used" ));

        //If it gets here, the validations were ok, so we create a new user
        User user = new User();

        Set<Role> roles = new HashSet<>();

        List<Role> rolesRepo = roleRepository.findAll();
        for (Role role : rolesRepo){
            if (role.getName().equalsIgnoreCase("ROLE_USER"))
                roles.add(role);
        }

        //Creates user without avatar nor hasAccess(subject)
        if (newUser.getAvatar() == null && newUser.getHasAccess() == null)
            user = new User(newUser.getEmail(), newUser.getUsername(),
                    encoder.encode(newUser.getPassword()), roles);

        //Creates user without avatar but with hasAccess(subject)
        if (newUser.getAvatar() == null && newUser.getHasAccess() != null)
            user = new User(newUser.getEmail(), newUser.getUsername(),
                    encoder.encode(newUser.getPassword()), roles, newUser.getHasAccess());

        //Creates user with avatar but without hasAccess(subject)
        if (newUser.getAvatar() != null && newUser.getHasAccess() == null)
            user = new User(newUser.getEmail(), newUser.getUsername(), encoder.encode(newUser.getPassword()),
                    roles, newUser.getAvatar());

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

        if (activationCode == user.getValidationCode()) {
            user.setActivated(true);
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The validation code is wrong"));
        }

        //Sends an email with a welcome message
        try {
            sparkPost.sendWelcomeMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .body(user.getDtoFromUser());
    }
}
