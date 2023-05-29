package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    @Override
    User save(User entity);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
