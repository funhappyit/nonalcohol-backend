package com.nonalcohol.backend.repository;
import com.nonalcohol.backend.dto.LabelCountDto;
import com.nonalcohol.backend.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Year;
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

    @Override
    public List<LabelCountDto> getAgeDistribution() {
        QMember member = QMember.member;

        // 문자열 그대로 "90년생" 형식으로 라벨 생성
        StringTemplate birthLabel = Expressions.stringTemplate("CONCAT({0}, '년생')", member.age);

        // WHERE age IN ('90' ~ '99', '00')
        BooleanExpression ageRange = member.age.in(
                "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "00"
        );

        return queryFactory
                .select(Projections.constructor(
                        LabelCountDto.class,
                        birthLabel,
                        member.count()
                ))
                .from(member)
                .where(ageRange)
                .groupBy(member.age)
                .orderBy(member.age.asc())
                .fetch();
    }











}
