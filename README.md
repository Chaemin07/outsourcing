![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=250&section=header&text=OutSourcing%20Project&fontSize=80)

## 개요
- Srping Boot, Spring Data JPA와 MySQL을 사용한 배달 어플 제작

## 개발 환경
언어 : ![Static Badge](https://img.shields.io/badge/Java-red?style=flat-square)

JDK : ![Static Badge](https://img.shields.io/badge/JDK-17-yellow?style=flat-square)

프레임워크 : ![Static Badge](https://img.shields.io/badge/SpringBoot-%23FFFF00?logo=springboot)

DB : ![Static Badge](https://img.shields.io/badge/MySql-%23FFFFFF?style=flat&logo=mysql)

ORM : ![Static Badge](https://img.shields.io/badge/JPA-FFA500?style=flat)

## 🔠 목차

1. [API 명세서](#-api-명세서)
2. [ERD](#-erd)
3. [기능 요약](#-기능-요약)
4. [디렉토리 구조](#-디렉토리-구조)

# API 명세서
![](https://cdn.gamma.app/pfxwn8km4qwffby/0b4f7ba276814f9cbe25033e81bef16c/original/main-til-04-23-2.png)

# ERD
![](https://cdn.gamma.app/pfxwn8km4qwffby/eea9339e5e0545fb8d410a089b297309/original/erd.png)

# 기능 요약
- 유저 CRUD
  - 사용자 역할 기반(일반/사장) 전용 기능
  - JWT 기반 인증/인가
- 가게 CRUD
  - 메뉴 CRUD
  - 메뉴 옵션 CRUD
  - 즐겨찾기
  - 카테고리별 필터링
- 리뷰 CRUD
  - 배달 완료 후 작성 
  - 별점 순 조회
  - 사진 있는 리뷰 조회
  - 배달 완료 후 3일 뒤 리뷰 작성, 수정 불가
- 주문 CRUD
  - 주문 상세
  - 장바구니 관리
  - 간편 결제 시스템

# 디렉토리 구조
```
├─main
│  ├─java
│  │  └─com
│  │      └─example
│  │          └─outsourcing
│  │              ├─address
│  │              │  ├─dto
│  │              │  ├─entity
│  │              │  └─service
│  │              ├─auth
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  └─service
│  │              ├─cart
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  ├─entity
│  │              │  ├─exception
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─category
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  ├─request
│  │              │  │  └─response
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─common
│  │              │  ├─annotation
│  │              │  ├─aop
│  │              │  │  └─log
│  │              │  ├─config
│  │              │  ├─entity
│  │              │  ├─enums
│  │              │  ├─exception
│  │              │  ├─initializer
│  │              │  └─response
│  │              ├─favorite
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  └─reponse
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─image
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  ├─service
│  │              │  └─util
│  │              ├─jwt
│  │              ├─menu
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  ├─request
│  │              │  │  └─response
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─order
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─payment
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  ├─entity
│  │              │  ├─exception
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─review
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  ├─request
│  │              │  │  └─response
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─reviewcomment
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  ├─request
│  │              │  │  └─response
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  └─service
│  │              ├─store
│  │              │  ├─controller
│  │              │  ├─dto
│  │              │  │  ├─request
│  │              │  │  └─response
│  │              │  ├─entity
│  │              │  ├─repository
│  │              │  ├─service
│  │              │  └─util
│  │              └─user
│  │                  ├─controller
│  │                  ├─dto
│  │                  ├─entity
│  │                  │  ├─controller
│  │                  │  ├─dto
│  │                  │  ├─repository
│  │                  │  └─service
│  │                  ├─repository
│  │                  └─service
│  └─resources
└─test
    └─java
        └─com
            └─example
                └─outsourcing
                    ├─cart
                    │  └─service
                    ├─store
                    │  └─service
                    └─user
                        └─service

```