package com.spb.studentportalbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "classes")
public class CourseClass extends BaseEntity{
    @Column(name="class_name",  nullable = false)
    String className;

    @Column(name = "class_code", nullable = false)
    String classCode;

    @Column(name = "max_capacity", nullable = false)
    Integer maxCapacity;

    @Column(name = "enrolled_count", nullable = false)
    Integer enrolledCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    User teacher;

    @ElementCollection
    @CollectionTable(
            name = "class_target_terms",
            joinColumns = @JoinColumn(name = "class_id")
    )
    Set<AcademicTerm> targetTerms = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "class_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    Set<User> enrolledStudents = new HashSet<>();

    @Version
    Long version;

    public boolean isFull() {
        return this.enrolledCount >= this.maxCapacity;
    }

}
