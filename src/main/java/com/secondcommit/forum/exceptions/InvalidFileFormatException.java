package com.secondcommit.forum.exceptions;

/**
 * Exception thrown when the API receives an unauthorized type file (not .jpeg o .jpg)
 */
public class InvalidFileFormatException extends Exception{
    public InvalidFileFormatException(String message){ super(message);}
}
