package com.nonalcohol.backend.repository;

import com.nonalcohol.backend.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {
    Page<Member> searchByKeyword(String keyword, Pageable pageable);
    List<Object[]> countByRegion();
}
