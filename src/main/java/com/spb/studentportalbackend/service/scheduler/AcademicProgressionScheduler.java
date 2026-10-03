package com.spb.studentportalbackend.service.scheduler;

import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcademicProgressionScheduler {

    UserRepository userRepository;

    // Runs Feb 1st at 00:00 Baku time
    @Scheduled(cron = "0 0 0 1 2 ?", zone = "Asia/Baku")
    @Transactional
    public void runFallToSpringTransition() {
        log.info("Starting Fall to Spring semester progression...");
        int count = userRepository.advanceFallToSpring();
        log.info("Successfully advanced {} students to Semester 2", count);
    }

    // Runs Sept 1st at 00:00 Baku time
    @Scheduled(cron = "0 0 0 1 9 ?", zone = "Asia/Baku")
    @Transactional
    public void runSpringToFallTransition() {
        log.info("Starting Spring to Fall academic year progression...");

        // Order matters! First graduate seniors, then promote underclassmen.
        int graduatedCount = userRepository.graduateFourthYearStudents();
        log.info("Successfully graduated {} 4th-year students", graduatedCount);

        int promotedCount = userRepository.advanceSpringToFall();
        log.info("Successfully promoted {} students to the next academic year", promotedCount);
    }
}