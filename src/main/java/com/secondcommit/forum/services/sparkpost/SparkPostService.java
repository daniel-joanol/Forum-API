package com.secondcommit.forum.services.sparkpost;

import com.secondcommit.forum.entities.User;
import com.sparkpost.exception.SparkPostException;
import org.springframework.http.ResponseEntity;

public interface SparkPostService {

    ResponseEntity<?> sendActivationMessage(User user) throws SparkPostException;
    ResponseEntity<?> sendWelcomeMessage(User user) throws SparkPostException;
    ResponseEntity<?> sendForgotPassMessage(User user) throws SparkPostException;
    ResponseEntity<?> sendChangedPassMessage(User user) throws SparkPostException;
    int randomNumber();
}
