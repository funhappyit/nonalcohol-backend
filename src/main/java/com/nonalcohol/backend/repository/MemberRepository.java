package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Query("SELECT m FROM Member m WHERE " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.region) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY CASE WHEN m.role = 'ROLE_ADMIN' THEN 0 ELSE 1 END, m.name")
    Page<Member> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m.region, COUNT(m) FROM Member m GROUP BY m.region")
    List<Object[]> countByRegion();
}

