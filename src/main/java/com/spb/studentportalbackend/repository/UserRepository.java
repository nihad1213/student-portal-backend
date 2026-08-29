package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByMail(String mail);

    boolean existsByRole(RoleEnum role);
}
