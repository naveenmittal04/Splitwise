package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<User, Long> {
    List<User> findAllById(Long groupId);
}
