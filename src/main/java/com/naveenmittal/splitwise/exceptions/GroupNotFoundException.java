package com.naveenmittal.splitwise.exceptions;

public class GroupNotFoundException extends Exception{
    public GroupNotFoundException() {
        super("Group Not Found");
    }
}
