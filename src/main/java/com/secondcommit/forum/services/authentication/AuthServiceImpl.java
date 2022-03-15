package com.secondcommit.forum.services.authentication;

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

/**
 * Implementation of the Authentication Service Interface
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final SparkPostServiceImpl sparkPost;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository,
                           SparkPostServiceImpl sparkPost){
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.sparkPost = sparkPost;
    }

    /**
     * Method that sends an email to the user with a code to be validated when the user tries to create a new password
     * @param user
     * @return
     */
    @Override
    public ResponseEntity<?> askNewPass(User user) {

        try {
            sparkPost.sendForgotPassMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok().body(new MessageResponse("Check your email account"));
    }

    /**
     * Method that change the password of the user
     * * The validation code must be correct for that to work!
     * @param user
     * @param newPass
     * @param validationCode
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> setNewPass(User user, String newPass, Integer validationCode) {

        //Validates the code
        if (validationCode != user.getValidationCode())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Wrong validation code"));

        //Saves the new password
        user.setPassword(encoder.encode(newPass));
        userRepository.save(user);

        //Sends email confirming that the password has been changed
        try {
            sparkPost.sendChangedPassMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .body(new MessageResponse("Password changed with success for " + user.getUsername()));
    }

}
