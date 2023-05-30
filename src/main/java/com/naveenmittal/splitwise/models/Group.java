package com.naveenmittal.splitwise.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "groupz")
public class Group extends BaseModel{
    private String name;
    @ManyToOne
    private User admin;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> members;

    public void addMember(User user) {
        if(this.members == null)
            this.members = new ArrayList<>();
        this.members.add(user);
    }
}
