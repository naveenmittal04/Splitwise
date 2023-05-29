package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.Group;
import com.naveenmittal.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Override
    Optional<Group> findById(Long groupId);

    Optional<Group> findByName(String name);
    @Override
    Group save(Group group);

    List<Group> findAllById(Long userId);

}
