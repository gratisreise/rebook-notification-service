# Rebook Notification Service

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.5-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.x-FF6600)
![Redis](https://img.shields.io/badge/Redis-6+-DC382D)
![Gradle](https://img.shields.io/badge/Gradle-8.14.2-02303A)

**Rebook 마이크로서비스 아키텍처의 실시간 알림 서비스**

RabbitMQ 기반 메시지 처리와 Server-Sent Events를 통한 실시간 알림 전송 시스템

</div>

---

## 1. 개요

**Rebook Notification Service**는 중고 도서 거래 플랫폼 Rebook의 핵심 백엔드 마이크로서비스로, 사용자에게 실시간 알림을 제공합니다. Spring Boot 기반으로 구현된 본 서비스는 **RabbitMQ 메시지 처리**, **Server-Sent Events (SSE)**, **서비스 디스커버리**를 통한 확장 가능한 구조를 제공합니다.


### 서비스 역할

본 서비스는 Rebook 플랫폼 내에서 다음과 같은 역할을 담당합니다:

- **실시간 알림 전송**: Server-Sent Events (SSE)를 통한 클라이언트 푸시 알림
- **메시지 기반 처리**: RabbitMQ를 통한 비동기 알림 생성 및 발송
- **알림 관리**: 알림 조회, 읽음 처리, 삭제 기능 제공
- **알림 설정**: 사용자별 알림 수신 설정 관리
- **외부 서비스 연동**: User Service, Book Service와 OpenFeign 연동

---

## 2. 목차

- [주요 기능](#3-주요-기능)
- [기술 스택](#4-기술-스택)
- [아키텍처](#5-아키텍처)
- [API 문서](#6-api-문서)
- [프로젝트 구조](#7-프로젝트-구조)

---

## 3. 주요 기능

### 3.1 실시간 알림 전송

#### Server-Sent Events (SSE)
- ✅ SSE 기반 실시간 푸시 알림
- ✅ 사용자별 독립적인 SSE 연결 관리
- ✅ 자동 재연결 및 타임아웃 처리 (10분)
- ✅ Heartbeat 메커니즘을 통한 연결 유지 (9분 주기)
- ✅ ConcurrentHashMap 기반 효율적 연결 관리
- ✅ 연결 실패 시 자동 에러 처리 및 정리

#### 알림 조회 및 관리
- ✅ 사용자별 알림 목록 조회 (페이지네이션 지원)
- ✅ 읽지 않은 알림 개수 조회
- ✅ 알림 읽음 상태 업데이트
- ✅ 알림 생성 시간 기준 정렬
- ✅ 타입별 알림 필터링 (BOOK, TRADE, CHAT, PAYMENT)

### 3.2 메시지 기반 알림 처리

#### RabbitMQ 메시지 처리
- ✅ 3가지 전용 큐 운영 (도서/거래/채팅)
- ✅ Topic Exchange 기반 메시지 라우팅
- ✅ JSON 메시지 자동 직렬화/역직렬화 (Jackson2JsonMessageConverter)
- ✅ 메시지 손실 방지를 위한 durable queue 설정
- ✅ 비동기 메시지 리스너를 통한 알림 생성

#### 알림 유형별 처리
- ✅ **도서 알림 (BOOK)**: 관심 카테고리 신규 도서 등록 시 알림
  - User Service에서 카테고리별 관심 사용자 조회
  - 해당 사용자들에게 일괄 알림 생성 및 전송
- ✅ **거래 알림 (TRADE)**: 찜한 도서 거래 가능 시 알림
  - Book Service에서 도서를 찜한 사용자 조회
  - 관심 사용자에게 거래 가능 알림 전송
- ✅ **채팅 알림 (CHAT)**: 새 채팅 메시지 도착 시 알림
  - 메시지 수신자에게 직접 알림 전송

### 3.3 알림 설정 관리

- ✅ 사용자별 알림 수신 설정 관리
- ✅ 알림 유형별 on/off 제어 (sendable 플래그)
- ✅ 자동 설정 생성 (최초 조회 시)
- ✅ 설정 업데이트 기능
- ✅ 복합키(Composite Key) 기반 효율적인 설정 관리

### 3.4 외부 서비스 연동 (Event-Driven)

- ✅ User Service 연동: 관심 카테고리별 사용자 목록 조회 (OpenFeign)
- ✅ Book Service 연동: 도서 찜한 사용자 목록 조회 (OpenFeign)
- ✅ Book Service 이벤트 수신: 신규 도서 등록 알림
- ✅ Trade Service 이벤트 수신: 거래 가능 알림
- ✅ Chat Service 이벤트 수신: 채팅 메시지 알림
- ✅ Eureka 기반 서비스 디스커버리


---

## 4. 기술 스택

### 4.1 백엔드 프레임워크

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Boot** | 3.3.13 | 애플리케이션 프레임워크 |
| **Java** | 17 | 프로그래밍 언어 |
| **Spring Data JPA** | - | ORM 및 데이터 접근 계층 |
| **Spring Validation** | - | 요청 데이터 유효성 검증 |
| **Lombok** | - | 보일러플레이트 코드 제거 |

### 4.2 데이터베이스 & 캐싱

| 기술 | 버전 | 용도 |
|------|------|------|
| **PostgreSQL** | 16 | 관계형 데이터베이스 (메인 데이터 저장소) |
| **Redis** | 6+ | 분산 캐싱 및 세션 관리 |

### 4.3 마이크로서비스 인프라 (Spring Cloud)

| 기술 | 버전 | 용도 |
|------|------|------|
| **Eureka Client** | 2023.0.5 | 서비스 디스커버리 및 등록 |
| **Spring Cloud Config** | 2023.0.5 | 중앙화된 설정 관리 (외부 Config Server) |
| **OpenFeign** | 2023.0.5 | 선언적 HTTP 클라이언트 (User/Book Service 연동) |

### 4.4 메시징 & 이벤트

| 기술 | 버전 | 용도 |
|------|------|------|
| **RabbitMQ (AMQP)** | 3.x | 비동기 메시징 및 이벤트 수신 |
| **Spring AMQP** | - | RabbitMQ 통합 및 메시지 컨버터 |

### 4.5 실시간 통신

| 기술 | 버전 | 용도 |
|------|------|------|
| **Server-Sent Events (SSE)** | - | 실시간 클라이언트 푸시 알림 |
| **Spring Web MVC** | - | SSE 연결 관리 및 이벤트 스트리밍 |

### 4.6 모니터링 & 로깅

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Actuator** | - | 헬스체크 및 메트릭 엔드포인트 |
| **Prometheus** | - | 메트릭 수집 및 모니터링 |
| **Sentry** | 8.13.2 | 실시간 에러 트래킹 및 알림 |
| **SLF4J & Logback** | - | 애플리케이션 로깅 |

### 4.7 API 문서화

| 기술 | 버전 | 용도 |
|------|------|------|
| **SpringDoc OpenAPI 3** | 2.6.0 | Swagger UI 기반 REST API 문서 자동 생성 |

### 4.8 빌드 & 배포

| 기술 | 버전 | 용도 |
|------|------|------|
| **Gradle** | 8.14.2 | 빌드 자동화 도구 |
| **Docker** | - | 컨테이너화 및 배포 |
| **Jacoco** | - | 테스트 커버리지 분석 |
| **JUnit 5** | - | 단위 테스트 프레임워크 |

---

## 5. 아키텍처

### 5.1 알림흐름
![알림흐름](https://rebook-bucket.s3.ap-northeast-2.amazonaws.com/rebook/notiflow.png)

### 5.2 Redis Pub/Sub 알림 처리
![알림흐름](https://rebook-bucket.s3.ap-northeast-2.amazonaws.com/rebook/redispubsub.png)


## 6. API 문서
Apidog: ~~~~

### 6.1 API 엔드포인트 상세

#### 6.1.1 SSE 연결 API (`SseController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **GET** | `/api/notifications/sse/connect` | SSE 구독 (실시간 알림 수신) |

#### 6.1.2 알림 관리 API (`NotificationController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **GET** | `/api/notifications/me` | 내 알림 목록 조회 (페이지네이션) |
| **GET** | `/api/notifications/me/numbers` | 읽지 않은 알림 개수 조회 |
| **PATCH** | `/api/notifications/{notificationId}` | 알림 읽음 처리 |

#### 6.1.3 알림 설정 API (`NotificationSettingController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **GET** | `/api/settings/{userId}` | 알림 설정 조회 |
| **PUT** | `/api/settings` | 알림 설정 업데이트 |


## 7. 프로젝트 구조

### 구조
```
rebook-notification-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/rebooknotificationservice/
│   │   │   ├── advice/                        # 전역 예외 처리
│   │   │   ├── common/                        # 공통 응답 모델
│   │   │   ├── config/                        # RabbitMQ, JPA 설정
│   │   │   ├── controller/                    # REST API 엔드포인트
│   │   │   ├── enums/                         # 알림 타입 열거형
│   │   │   ├── exception/                     # 커스텀 예외 클래스
│   │   │   ├── feigns/                        # 외부 서비스 연동 (User/Book)
│   │   │   ├── model/                         # 엔티티, DTO, 메시지 모델
│   │   │   ├── repository/                    # JPA 리포지토리
│   │   │   ├── service/                       # 비즈니스 로직
│   │   │   └── RebookNotificationServiceApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-dev.yaml
│   │       └── application-prod.yaml
│   │
│   └── test/
│       └── java/com/example/rebooknotificationservice/
│
├── build.gradle
├── Dockerfile
└── README.md
```
