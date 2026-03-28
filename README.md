# Rebook Notification Service

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)

Rebook 플랫폼의 실시간 알림 마이크로서비스입니다. SSE(Server-Sent Events) 기반으로 도서 등록, 교환 요청, 채팅 메시지 등 다양한 이벤트에 대한 알림을 사용자에게 실시간으로 전달합니다.

## 목차

- [기능](#기능)
- [기술 스택](#기술-스택)
- [API 문서](#api-문서)
- [프로젝트 구조](#프로젝트-구조)
- [RabbitMQ 큐 구성](#rabbitmq-큐-구성)

---

## 기능

### 알림 유형

| 타입 | 설명 | 트리거 |
|------|------|--------|
| **BOOK** | 도서 알림 | 관심 카테고리에 새 도서 등록 시 |
| **TRADE** | 교환 알림 | 관심 도서가 교환에 등록될 때 |
| **CHAT** | 채팅 알림 | 새 메시지 수신 시 |

### 주요 기능

- **실시간 알림 전송**: SSE(Server-Sent Events) 기반 실시간 통신 (60초 타임아웃)
- **Heartbeat**: 15초 간격 ping으로 연결 유지
- **알림 영속화**: PostgreSQL에 알림 이력 저장 및 읽음 처리
- **알림 설정**: 사용자별 알림 타입 수신 여부 관리
- **DLQ 처리**: 메시지 처리 실패 시 Dead Letter Queue로 자동 이동
- **분산 전달**: Redis Pub/Sub 기반 다중 인스턴스 알림 분산

---

## 기술 스택

### Language & Framework
- **Java 17**, **Spring Boot 3.x**, **Spring Cloud**

### Cloud & Service Discovery
- **Eureka**, **Config Server**, **OpenFeign**

### Database
- **PostgreSQL**, **QueryDSL**, **Spring Data JPA**

### Messaging
- **RabbitMQ** (AMQP) — 알림 메시지 비동기 처리
- **Redis** (Pub/Sub) — 다중 인스턴스 알림 분산

### Monitoring & Docs
- **Actuator**, **Prometheus**, **Sentry**
- **SpringDoc OpenAPI** (Swagger UI)

### Build & Deploy
- **Gradle**, **Docker**

---

## API 문서

애플리케이션 실행 후 Swagger UI에서 전체 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui.html
```

### 알림 조회

| Method | Endpoint | 설명 |
|--------|----------|------|
| `GET` | `/api/notifications/me` | 내 알림 목록 조회 (페이지네이션) |
| `GET` | `/api/notifications/me/numbers` | 읽지 않은 알림 개수 |

### 알림 읽음 처리

| Method | Endpoint | 설명 |
|--------|----------|------|
| `PATCH` | `/api/notifications/{notificationId}` | 알림 읽음 처리 |

### 알림 설정

| Method | Endpoint | 설명 |
|--------|----------|------|
| `GET` | `/api/notifications/me/settings` | 내 알림 설정 조회 |
| `POST` | `/api/notifications/me/settings` | 알림 설정 생성 |
| `PATCH` | `/api/notifications/settings?type={type}` | 알림 설정 토글 (BOOK/TRADE/CHAT) |

### SSE 연결

| Method | Endpoint | 설명 |
|--------|----------|------|
| `GET` | `/api/notifications/sse/connect` | SSE 실시간 연결 |

> SSE 연결 시 `X-User-Id` 헤더가 필요합니다. API Gateway에서 인증 후 자동 주입됩니다.

**SSE 이벤트:**

| 이벤트명 | 데이터 | 설명 |
|---------|--------|------|
| `connected` | `"connected"` | 연결 성공 확인 |
| `notification` | 알림 메시지 | 새 알림 도착 |
| `heartbeat` | `"ping"` | 15초 간격 연결 유지 |

---

## 프로젝트 구조

```
src/main/java/.../rebooknotificationservice/
├── clientfeign/         # 외부 서비스 통신 (BookClient, UserClient)
├── common/enums/        # 알림 타입 (BOOK, TRADE, CHAT)
├── domain/
│   ├── controller/      # REST API & SSE 엔드포인트
│   ├── model/           # DTO & Entity (Notification, NotificationSetting)
│   ├── repository/      # JPA Repository
│   └── service/         # 비즈니스 로직 (reader/writer 분리)
├── external/
│   ├── rabbitmq/        # 큐 설정, 메시지 구독/발행, DTO
│   └── redis/           # Redis Pub/Sub 설정
└── exception/           # NotificationException
```

---

## RabbitMQ 큐 구성

| 큐 이름 | Exchange | Routing Key | DLQ |
|---------|----------|-------------|-----|
| `book.notification.queue` | `book.exchange` | `book.notification` | `book.notification.dlq` |
| `trade.notification.queue` | `trade.exchange` | `trade.notification` | `trade.notification.dlq` |
| `chat.notification.queue` | `chat.exchange` | `chat.notification` | `chat.notification.dlq` |