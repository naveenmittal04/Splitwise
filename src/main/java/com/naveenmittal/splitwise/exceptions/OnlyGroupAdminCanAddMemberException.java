package com.naveenmittal.splitwise.exceptions;

public class OnlyGroupAdminCanAddMemberException extends Exception{
    public OnlyGroupAdminCanAddMemberException() {
        super("Permission Denied: Only Group Admin Can Add Member");
    }
}
