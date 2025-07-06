package com.nonalcohol.backend.repository;
import com.nonalcohol.backend.entity.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchByKeyword(String keyword, Pageable pageable) {
        QMember m = QMember.member;

        JPQLQuery<Member> query = queryFactory
                .selectFrom(m)
                .where(
                        m.name.containsIgnoreCase(keyword)
                                .or(m.region.containsIgnoreCase(keyword))
                )
                .orderBy(
                        new CaseBuilder()
                                .when(m.role.eq("ROLE_ADMIN")).then(0)
                                .otherwise(1).asc(),
                        m.name.asc()
                );

        List<Member> members = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(m.count())
                .from(m)
                .where(
                        m.name.containsIgnoreCase(keyword)
                                .or(m.region.containsIgnoreCase(keyword))
                )
                .fetchOne();

        return new PageImpl<>(members, pageable, total);
    }

    @Override
    public List<Object[]> countByRegion() {
        QMember m = QMember.member;

        return queryFactory
                .select(m.region, m.count())
                .from(m)
                .groupBy(m.region)
                .fetch()
                .stream()
                .map(t -> new Object[]{t.get(m.region), t.get(m.count())})
                .collect(Collectors.toList());
    }
}
