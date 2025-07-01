package com.nonalcohol.backend.repository;
import com.nonalcohol.backend.dto.AttendanceStatDto;
import com.nonalcohol.backend.dto.QAttendanceStatDto;
import com.nonalcohol.backend.entity.QAttendance;
import com.nonalcohol.backend.entity.QEvent;
import com.nonalcohol.backend.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

}
