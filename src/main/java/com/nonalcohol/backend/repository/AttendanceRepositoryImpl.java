package com.nonalcohol.backend.repository;
import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.dto.QAttendanceStatDto;
import com.nonalcohol.backend.dto.WeeklyParticipationDto;
import com.nonalcohol.backend.entity.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttendanceStatDto> countAttendanceStatsByMonth(String month) {
        int monthInt = Integer.parseInt(month); // 🔹 int로 변환
        String formattedMonth = String.format("%02d", monthInt); // 🔹 두 자리로 포맷

        QAttendance a = QAttendance.attendance;
        QMember m = QMember.member;
        QEvent e = QEvent.event;

        return queryFactory
                .select(new QAttendanceStatDto(
                        m.id,
                        m.name,
                        m.username,
                        a.count(),
                        m.age
                ))
                .from(m)
                .leftJoin(a).on(
                        a.member.eq(m)
                                .and(a.status.eq("참석"))
                                .and(a.event.date.substring(5, 7).eq(formattedMonth)) // ✅ 수정된 부분
                )
                .groupBy(m.id)
                .fetch();
    }

    @Override
    public List<Attendance> findByEventWithMember(Event event) {
        QAttendance a = QAttendance.attendance;
        QMember m = QMember.member;

        return queryFactory
                .selectFrom(a)
                .join(a.member, m).fetchJoin()
                .where(a.event.eq(event))
                .fetch();
    }

    @Override
    public List<Attendance> findByEvent(Event event) {
        QAttendance a = QAttendance.attendance;
        return queryFactory
                .selectFrom(a)
                .where(a.event.eq(event))
                .fetch();
    }

    @Override
    public List<Object[]> countAttendanceNative(String start, String end) {
        QAttendance a = QAttendance.attendance;
        QMember m = QMember.member;
        QEvent e = QEvent.event;

        return queryFactory
                .select(m.name, a.count())
                .from(a)
                .join(a.member, m)
                .join(a.event, e)
                .where(
                        a.status.eq("참석")
                                .and(e.date.between(start, end))
                )
                .groupBy(m.name)
                .fetch()
                .stream()
                .map(tuple -> new Object[]{tuple.get(m.name), tuple.get(a.count())})
                .collect(Collectors.toList());
    }

    @Override
    public List<WeeklyParticipationDto> getWeeklyParticipation() {
        QAttendance attendance = QAttendance.attendance;
        QEvent event = QEvent.event;

        // 🔹 주차 구분 표현식 (cast 제거)
        Expression<String> weekExpr = Expressions.stringTemplate(
                "concat(substring({0}, 6, 2), '월 ', " +
                        "case " +
                        "when substring({0}, 9, 2) <= '07' then '1주차' " +
                        "when substring({0}, 9, 2) <= '14' then '2주차' " +
                        "when substring({0}, 9, 2) <= '21' then '3주차' " +
                        "when substring({0}, 9, 2) <= '28' then '4주차' " +
                        "else '5주차' end)",
                event.date
        );

        // 🔹 주차 기준 오름차순 정렬
        OrderSpecifier<String> orderByWeek = new OrderSpecifier<>(
                Order.ASC,
                weekExpr
        );

        return queryFactory
                .select(Projections.constructor(
                        WeeklyParticipationDto.class,
                        weekExpr,
                        attendance.count()
                ))
                .from(attendance)
                .join(attendance.event, event)
                .where(attendance.status.eq("참석"))
                .groupBy(weekExpr)
                .orderBy(orderByWeek)
                .fetch();
    }






}
