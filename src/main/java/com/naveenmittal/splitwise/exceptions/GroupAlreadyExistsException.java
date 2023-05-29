package com.naveenmittal.splitwise.exceptions;

public class GroupAlreadyExistsException extends Exception{
    public GroupAlreadyExistsException() {
        super("Group Already Exists");
    }
}
