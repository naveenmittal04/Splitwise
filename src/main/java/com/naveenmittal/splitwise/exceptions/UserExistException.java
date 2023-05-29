package com.naveenmittal.splitwise.exceptions;

public class UserExistException extends Exception{
    public UserExistException() {
        super("User already exist");
    }
}
