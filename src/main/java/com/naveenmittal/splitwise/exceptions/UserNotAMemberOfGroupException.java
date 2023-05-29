package com.naveenmittal.splitwise.exceptions;

public class UserNotAMemberOfGroupException extends Exception{
    public UserNotAMemberOfGroupException() {
        super("User is not a member of the group");
    }
}
