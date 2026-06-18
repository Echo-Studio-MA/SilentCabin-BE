# Silent-Cabin 프로젝트 가이드

## 프로젝트 개요

Unity 게임 클라이언트의 백엔드 서버. Unity가 REST API로 게임 플레이 데이터를 전송하면 백엔드가 저장하고, **대시보드는 Thymeleaf + Chart.js로 웹페이지를 직접 서빙**한다.

## 기술 스택

| 항목 | 선택 |
|------|------|
| Java | 25 |
| Spring Boot | 4.1.0 |
| Spring Security | JWT 기반 Stateless |
| ORM | Spring Data JPA + Hibernate |
| DB | MySQL (운영) / H2 (테스트) |
| 뷰 템플릿 | Thymeleaf (`spring-boot-starter-thymeleaf`) |
| 그래프 | Chart.js (CDN) |
| 빌드 | Gradle |
| 유틸 | Lombok |

## 패키지 구조

```
io.echo.silentcabin
├── common
│   ├── config
│   │   ├── filter/         # JwtAuthenticationFilter
│   │   ├── entrypoint/     # 401/403 핸들러
│   │   └── properties/     # JwtProperties
│   ├── domain/             # BaseEntity, UpdateEntity
│   ├── dto/                # ApiResponse<T>
│   ├── exception/          # CabinException, GlobalExceptionHandler, ErrorCode
│   └── success/            # SuccessCode
├── user
│   ├── controller/         # UserController (REST)
│   ├── domain/             # User, Role, RefreshToken
│   ├── dto/
│   ├── repository/
│   └── service/            # UserService, TokenProvider
└── play
    ├── controller/
    │   ├── GamePlayController.java     # @RestController - Unity REST API
    │   └── DashboardController.java    # @Controller - Thymeleaf 웹 대시보드 (추가 예정)
    ├── domain/             # GamePlay
    ├── dto/                # GamePlaySaveRequestDto
    ├── repository/         # GamePlayRepository
    └── service/            # GamePlayService
```

## 핵심 도메인: GamePlay

Unity에서 게임 종료 시 전송하는 플레이 기록.

```java
GamePlay {
    Long id
    Integer score       // 점수
    Long clearTime      // 클리어에 걸린 시간 (초), startedAt ~ endedAt 자동 계산
    Boolean isClear     // 클리어 성공 여부
    LocalDateTime startedAt
    LocalDateTime endedAt
    User user           // ManyToOne (LAZY)
    // BaseEntity: createdAt, updatedAt
}
```

## 대시보드 구현 방향

**접근법: 차트를 여러 개 먼저 만들고, 유효한 것만 추려 대시보드로 확정한다.**

### 만들 수 있는 차트 후보

| 차트 | 데이터 | 유형 |
|------|--------|------|
| 점수 TOP N 랭킹 | score, user.nickname | Bar / Horizontal Bar |
| 클리어 시간 최단 랭킹 | clearTime (isClear=true), user.nickname | Bar |
| 클리어율 | isClear 비율 | Doughnut / Pie |
| 일별 플레이 수 | createdAt 날짜별 count | Line |
| 유저별 플레이 횟수 | user.nickname, count | Bar |
| 점수 분포 | score 구간별 count | Bar (히스토그램) |

### DashboardController 역할

- `@Controller` (Thymeleaf, 뷰 이름 반환)
- `play.controller` 패키지에 위치
- `GamePlayService`를 통해 통계 데이터 조회 후 `Model`에 담아 템플릿으로 전달
- URL: `/dashboard/**`

### 파일 위치

```
src/main/resources/
├── templates/
│   └── dashboard/
│       └── index.html      # Chart.js 차트들을 모아둔 대시보드 페이지
└── static/
    ├── css/
    └── js/
```

## REST vs 웹 컨트롤러 구분

| 용도 | 어노테이션 | 응답 |
|------|-----------|------|
| Unity API | `@RestController` | JSON (`ApiResponse<T>`) |
| 웹 대시보드 | `@Controller` | Thymeleaf 뷰 이름 (String) |

## 코드 컨벤션

- **Lombok**: `@Getter`, `@RequiredArgsConstructor`, `@NoArgsConstructor(access = PROTECTED)`
- **엔티티**: setter 금지, protected 기본 생성자 + 필요한 필드만 받는 public 생성자
- **모든 엔티티는 BaseEntity 상속**
- **REST 응답은 `ApiResponse<T>` 공통 래퍼**
- **예외는 `CabinException(ErrorCode)`** → `GlobalExceptionHandler` 처리
- 주석은 WHY가 명확할 때만, 최소한으로

## Security 주의사항

현재 설정: Stateless (세션 없음), CSRF disabled.

대시보드 경로 추가 시 `SecurityConfig`에 아래 경로를 허용해야 함:
```java
.requestMatchers("/dashboard/**", "/css/**", "/js/**").permitAll()
```

Thymeleaf 웹 폼을 추가할 경우 CSRF 재활성화 여부 검토 필요.

## build.gradle 추가 의존성

```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
```

## 환경 변수 (.env)

```
dbUrl=
dbUserName=
dbPassWord=
jwt.app-key=
jwt.origin-key=
```

## 자주 쓰는 명령

```bash
./gradlew bootRun   # 실행
./gradlew test      # 테스트
./gradlew build     # 빌드
```
