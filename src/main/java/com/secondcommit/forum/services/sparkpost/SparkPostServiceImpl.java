package com.secondcommit.forum.services.sparkpost;

import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

/**
 * Service that manages SparKPost (to send emails)
 */
@Service
public class SparkPostServiceImpl implements SparkPostService{

    private final String API_KEY = System.getenv("SPARK_POST_API");
    Client client = new Client(API_KEY);

    private UserRepository userRepository;

    public SparkPostServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method sends an activation code to the user email in order to validate his/her email account
     *
     * @param user
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> sendActivationMessage(User user) throws SparkPostException {

        //Generates a new random number
        int validationCode = randomNumber();

        //Sends email
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "Your activation code",
                    "Hello, your activation code is: " + validationCode +  "\n"
                            + "Remember that your code expires in 5 minutes",
                    "Hello, your activation code is: " + validationCode);
        } catch (SparkPostException e){
            e.printStackTrace();
        }

        //Saves the new validation code
        user.setActivationCode(validationCode);
        user.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        return ResponseEntity.ok().body(new MessageResponse("Check your email account"));
    }

    /**
     * Welcome message sent after activating the account
     * @param user
     * @return ResponseEntity
     * @throws SparkPostException
     */
    @Override
    public void sendWelcomeMessage(User user) throws SparkPostException {
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "Welcome to the OpenBootcamp Forum!",
                    "Welcome, " + user.getUsername() + ". Your account has been activated with success",
                    "Welcome, " + user.getUsername() + ". Your account has been activated with success");
        } catch (SparkPostException e){
            e.printStackTrace();
        }
    }

    /**
     * This method sends a verification code to the user email so he can change his/her password
     * First it validates the email, if the email isn't right the email won't be sent
     *
     * @param user
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> sendForgotPassMessage(User user) throws SparkPostException {

        //Generates a new random number
        int validationCode = randomNumber();

        //Sends email
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "Forgot your password?",
                    "Hello, your validation code is: " + validationCode + "\n"
                            + "Remember that your code expires in 5 minutes",
                    "Hello, your validation code is: " + validationCode);
        } catch (SparkPostException e){
            e.printStackTrace();
        }

        //Saves the new validation code
        user.setValidationCode(validationCode);
        user.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        return ResponseEntity.ok().body(new MessageResponse("Check your email account"));
    }

    @Override
    public void sendChangedPassMessage(User user) throws SparkPostException {
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "Changed password!",
                    "Hi, " + user.getUsername() + ". Your password has been changed with success",
                    "Hi, " + user.getUsername() + ". Your password has been changed with success");
        } catch (SparkPostException e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendUserUpdatedMessage(User user) throws SparkPostException {
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "User updated!",
                    "Hi, " + user.getUsername() + ". Your credentials have been updated with success",
                    "Hi, " + user.getUsername() + ". Your credentials have been updated with success");
        } catch (SparkPostException e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendUserRemovedMessage(User user) throws SparkPostException {
        try {
            client.sendMessage(
                    "contacto@ob.danieljoanol.com",
                    user.getEmail(),
                    "User deleted!",
                    "Hi, " + user.getUsername() + ". Your credential have been removed from our database with success",
                    "Hi, " + user.getUsername() + ". Your credential have been removed from our database with success");
        } catch (SparkPostException e){
            e.printStackTrace();
        }
    }

    @Override
    public int randomNumber() {
        Random random = new Random();
        return random.nextInt(1000000);
    }
}
