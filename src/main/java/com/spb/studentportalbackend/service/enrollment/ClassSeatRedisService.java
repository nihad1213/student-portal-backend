package com.spb.studentportalbackend.service.enrollment;

import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassSeatRedisService {

    public enum ReserveResult { RESERVED, ALREADY_ENROLLED, FULL, NOT_INITIALIZED }

    private static final Duration TTL = Duration.ofHours(6);

    private static final RedisScript<Long> RESERVE_SCRIPT = new DefaultRedisScript<>("""
            if redis.call('EXISTS', KEYS[1]) == 0 then return -1 end
            if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then return -2 end
            if tonumber(redis.call('GET', KEYS[1])) <= 0 then return -3 end
            redis.call('DECR', KEYS[1])
            redis.call('SADD', KEYS[2], ARGV[1])
            return 1
            """, Long.class);

    private static final RedisScript<Long> RELEASE_SCRIPT = new DefaultRedisScript<>("""
            if redis.call('SREM', KEYS[2], ARGV[1]) == 1 then
                if redis.call('EXISTS', KEYS[1]) == 1 then redis.call('INCR', KEYS[1]) end
            end
            return 1
            """, Long.class);

    private final StringRedisTemplate redis;
    private final CourseClassRepository courseClassRepository;

    public ReserveResult reserve(Long classId, Long studentId) {
        Long result = redis.execute(RESERVE_SCRIPT, keys(classId), studentId.toString());
        if (result == null) {
            throw new IllegalStateException("Redis returned null for reserve script");
        }
        return switch (result.intValue()) {
            case 1 -> ReserveResult.RESERVED;
            case -2 -> ReserveResult.ALREADY_ENROLLED;
            case -3 -> ReserveResult.FULL;
            default -> ReserveResult.NOT_INITIALIZED;
        };
    }

    public void release(Long classId, Long studentId) {
        try {
            redis.execute(RELEASE_SCRIPT, keys(classId), studentId.toString());
        } catch (Exception e) {
            // Counter may drift until the TTL expires and the keys are rebuilt from the DB
            log.error("Failed to release seat classId={} studentId={}", classId, studentId, e);
        }
    }

    /** Loads capacity and enrolled students from the DB into Redis. */
    public void warmUp(Long classId) {
        CourseClass courseClass = courseClassRepository.findById(classId)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        String seatsKey = seatsKey(classId);
        String studentsKey = studentsKey(classId);

        List<Long> enrolledIds = courseClassRepository.findEnrolledStudentIds(classId);
        if (!enrolledIds.isEmpty()) {
            redis.opsForSet().add(studentsKey, enrolledIds.stream().map(String::valueOf).toArray(String[]::new));
            redis.expire(studentsKey, TTL);
        }

        int remaining = courseClass.getMaxCapacity() - courseClass.getEnrolledCount();
        // setIfAbsent: if two threads warm up at once, only the first one wins
        Boolean set = redis.opsForValue().setIfAbsent(seatsKey, String.valueOf(Math.max(remaining, 0)), TTL);
        log.info("Warmed up class seats classId={} remaining={} created={}", classId, remaining, set);
    }

    /** Call when capacity changes or the class is deleted. Next enroll re-warms from the DB. */
    public void evict(Long classId) {
        redis.delete(List.of(seatsKey(classId), studentsKey(classId)));
    }

    private List<String> keys(Long classId) {
        return List.of(seatsKey(classId), studentsKey(classId));
    }

    private String seatsKey(Long classId) { return "class:" + classId + ":seats"; }
    private String studentsKey(Long classId) { return "class:" + classId + ":students"; }
}