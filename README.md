# 🧼 논알콜 방관리 프로젝트

> 90~00년생 여성들의 **건전한 논알콜 모임**을 위한 웹 기반 방(벙) 관리 시스템입니다.  
> 카카오 오픈채팅 기반으로 운영되며, 모임의 **참여, 통계, 신입관리, 순위, 지역/연령 분포** 등 다양한 기능을 제공합니다.

## 📽️ 데모 영상

👉 [데모 영상 보기 (Google Drive)](https://drive.google.com/file/d/1jpNu3L5mm8KJZuXn3U00iA_Kb6SDH1dt/view?usp=sharing)

## 📁 프로젝트 구조
nonalcohol/
├── backend/ # Spring Boot + JPA + MyBatis
├── frontend/ # Vue.js + DevExtreme
└── README.md
## 🛠 기술 스택

| 영역      | 기술                                                         |
|----------|--------------------------------------------------------------|
| Frontend | Vue 3, Vite, DevExtreme UI, Axios                            |
| Backend  | Java 17, Spring Boot, JPA, MyBatis, QueryDSL                 |
| Database | MySQL                                                        |
| 기타     | Kakao 오픈채팅 기반 운영, macOS 개발환경, Notion 일정 관리     |

## 📌 주요 기능

### ✅ 회원 관리
- 회원 등록/조회/검색/삭제
- 신입 여부 관리 (가입일 기준 한 달 이내 1회 참석 조건)
- DevExtreme 적용한 그리드 UI

### 📊 통계 및 대시보드
- 월별 벙 참여 순위 (차트 및 표 형태)
- 지역별 회원 수
- 연령별 회원 수
- 미참여자 리스트

### 📆 벙(이벤트) 관리
- 벙 생성/수정/삭제
- 벙 상세 보기 및 참석자 목록 조회

### 🏅 랭킹
- 월별 참석 횟수에 따른 개인 순위
- 미참여자 자동 분리

## 🧪 개발 환경

- **OS:** macOS
- **IDE:** IntelliJ / VSCode
- **DB Tool:** DBeaver
- **API 테스트:** Postman
- **협업 도구:** Notion, GitHub

## 📌 개발 히스토리 요약

| 날짜       | 작업 내용                                                         |
|------------|------------------------------------------------------------------|
| 2025.06.18 | 프로젝트 세팅 및 ERD 작성, JPA & MyBatis 혼용 구조 구성         |
| 2025.06.24 | 회원, 벙 테이블 설계 및 Spring Entity 생성                      |
| 2025.06.27 | 신입 조건 로직 추가 (joinedDate & attendanceDeadline 관리)      |
| 2025.06.28 | 벙 수정/삭제 기능 프론트에서 구현                               |
| 2025.07.01 | 대시보드 차트 1차 구성 시작 (벙 참여 순위, 지역 통계 등)        |
| 2025.07.07 | 주간 벙 참가자 통계 쿼리 개선 및 월별 기준 조회 기능 구현       |
| 2025.07.16 | 회원 관리 UI 구현                               |
| 2025.07.19 | 미참여자 구분 로직 및 computed 처리 최적화                      |
| 2025.07.21 | Vue에서 월 선택에 따른 순위 재정렬 기능 구현                    |

## 📝 사용 방법 (로컬 개발)

```bash
# 백엔드 실행
cd backend
./gradlew bootRun

# 프론트엔드 실행
cd frontend
npm install
npm run dev

📂 향후 개선사항
JWT 기반 로그인 인증 기능 도입

Admin 전용 페이지 분리

모임 리마인더 알림 기능 추가

다크모드 및 모바일 대응 UI 개선

© 2025. 논알콜 방관리 프로젝트 — Made with 💛 by 한유경
