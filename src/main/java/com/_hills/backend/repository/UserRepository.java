package com._hills.backend.repository;

import com._hills.backend.entity.User;
import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndProfileStatus(Role role, ProfileStatus profileStatus);
}
