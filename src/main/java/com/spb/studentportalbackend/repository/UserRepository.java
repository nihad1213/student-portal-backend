package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByMail(String mail);

    boolean existsByMailAndIdNot(String mail, Long id);

    boolean existsByRole(RoleEnum role);
}
