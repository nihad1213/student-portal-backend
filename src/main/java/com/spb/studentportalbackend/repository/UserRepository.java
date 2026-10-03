package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByMail(String mail);

    boolean existsByMailAndIdNot(String mail, Long id);

    boolean existsByRole(RoleEnum role);

    @Modifying
    @Query("UPDATE User u SET u.currentSemester = 2 " +
            "WHERE u.role = 'STUDENT' AND u.currentSemester = 1 " +
            "AND u.frozen = false AND u.graduated = false AND u.enabled = true")
    int advanceFallToSpring();

    @Modifying
    @Query("UPDATE User u SET u.currentSemester = 1, u.currentYear = u.currentYear + 1 " +
            "WHERE u.role = 'STUDENT' AND u.currentSemester = 2 AND u.currentYear < 4 " +
            "AND u.frozen = false AND u.graduated = false AND u.enabled = true")
    int advanceSpringToFall();

    @Modifying
    @Query("UPDATE User u SET u.graduated = true, u.enabled = false " +
            "WHERE u.role = 'STUDENT' AND u.currentSemester = 2 AND u.currentYear = 4 " +
            "AND u.frozen = false AND u.graduated = false AND u.enabled = true")
    int graduateFourthYearStudents();
}
