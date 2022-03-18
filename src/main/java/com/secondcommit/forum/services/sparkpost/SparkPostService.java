package com.secondcommit.forum.services.sparkpost;

import com.secondcommit.forum.entities.User;
import com.sparkpost.exception.SparkPostException;
import org.springframework.http.ResponseEntity;

public interface SparkPostService {

    ResponseEntity<?> sendActivationMessage(User user) throws SparkPostException;
    void sendWelcomeMessage(User user) throws SparkPostException;
    ResponseEntity<?> sendForgotPassMessage(User user) throws SparkPostException;
    void sendChangedPassMessage(User user) throws SparkPostException;
    void sendUserUpdatedMessage(User user) throws SparkPostException;
    void sendUserRemovedMessage(User user) throws SparkPostException;
    int randomNumber();
}
