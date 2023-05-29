package com.naveenmittal.splitwise.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity(name = "groups_table")
public class Group extends BaseModel{
    private String name;
    @ManyToOne
    private User admin;
    @ManyToMany
    private List<User> members;
}
