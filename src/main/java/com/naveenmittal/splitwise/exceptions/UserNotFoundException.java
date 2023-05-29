package com.naveenmittal.splitwise.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User Not Found");
    }
}
