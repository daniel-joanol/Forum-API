package com.secondcommit.forum.exceptions;

/**
 * Exception thrown when the file received is empty
 */
public class EmptyFileException extends Exception {
    public EmptyFileException(String message) { super(message); }
}
