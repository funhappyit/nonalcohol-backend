package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
