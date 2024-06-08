## 목차
+ [프로젝트 소개](#프로젝트-소개)
+ [프로젝트 기간](#프로젝트-기간)
+ [개발 도구 및 환경](#개발-도구-및-환경)
+ [Use Case Diagram](#use-case-diagram)
+ [ERD](#erd)
+ [API 명세서](#api-명세서)
+ [결과 화면](#결과-화면)

## 프로젝트 소개
Todo 관리와 댓글로 소통할 수 있는 서비스

## 프로젝트 기간
2024.05.10 ~ (진행중)

## 개발 도구 및 환경
<img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/jdk17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/springboot3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://camo.githubusercontent.com/3c0e585bf7fbcca3f142c9c5de6bf415189bfebf5ebe71f59d2efd6272fd8d10/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672044617461204a70612d3644423333463f7374796c653d666f722d7468652d6261646765266c6f676f3d737072696e67266c6f676f436f6c6f723d7768697465"> 
<img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white"> <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white"/> <img src="https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white">

---
<details>
    <summary>STEP 1 : Use Case Diagram, ERD, API 명세서</summary><div>

<b>Use Case Diagram</b>  
![USECASEDIAGRAM](https://github.com/hellou8363/todolist/assets/89592727/f4cbf471-fc3f-43b6-8167-4c3cf8e27b04)

<b>ERD</b>  
![step1-erd](https://github.com/hellou8363/todolist/assets/89592727/2734bbae-1764-4ceb-b484-9a8044a3d352)

<b>API 명세서</b>  
![step1-api](https://github.com/hellou8363/todolist/assets/89592727/9f11a462-669d-4446-8e09-055474dce70f)

</div></details>
<details>
    <summary>STEP 2 : Use Case Diagram, ERD, API 명세서</summary><div>

<b>Use Case Diagram</b>  
![step2-usecasediagram](https://github.com/hellou8363/todolist/assets/89592727/9cbee50e-f1b3-4beb-a84f-2f8a08b63b18)

<b>ERD</b>  
![step2-erd](https://github.com/hellou8363/todolist/assets/89592727/5151142b-0aab-412a-86f9-575ab9735a83)

<b>API 명세서</b>  
![step2-api](https://github.com/hellou8363/todolist/assets/89592727/ed4c86c6-aa35-49b9-bbb5-09d3867c32b4)

</div></details>
<details>
    <summary>STEP 3 : Use Case Diagram, ERD, API 명세서</summary><div>

<b>Use Case Diagram</b>  
![step3-api](https://github.com/hellou8363/todolist/assets/89592727/bb65c070-cdef-435a-bd8e-3299f9b6b764)

<b>ERD</b>  
![step2-erd](https://github.com/hellou8363/todolist/assets/89592727/5151142b-0aab-412a-86f9-575ab9735a83)

<b>API 명세서</b>  
![step3-api](https://github.com/hellou8363/todolist/assets/89592727/85e6c0e6-7fe9-492c-955d-56382ac3040c)

</div></details>

## Use Case Diagram
![step4-usecasediagram](https://github.com/hellou8363/todolist/assets/89592727/5f87dd2e-5123-4e0d-aee7-cf1b13700e2c)

## ERD
![step4-erd](https://github.com/hellou8363/todolist/assets/89592727/4be336be-b224-4b8f-8b1b-f37568c9f8e6)

## API 명세서
![api-4](https://github.com/hellou8363/todolist/assets/89592727/6d5d7680-9363-4df9-bcac-43b6c3997f82)

## 테이블 생성
``` 
CREATE TABLE todo (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    writer TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed TEXT NOT NULL
);

CREATE TABLE comment (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    writer TEXT NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    todo_id BIGINT,
    constraint fk_comment_todo FOREIGN KEY (todo_id) REFERENCES todo ON DELETE CASCADE
);

CREATE TABLE todo_user (
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    nickname TEXT NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
## 패키지 구조
```
org.zerock.todolist
└── config
    └── auth
      └── filter
      └── handler
      └── util
└── domain
    ├── comment
    |   ├── controller
    |   ├── dto
    |   ├── model
    |   ├── repository
    |   └── service
    ├── exception
    |   └── dto
    ├── todo
    |   ├── controller
    |   ├── dto
    |   ├── model
    |   ├── repository
    |   └── service
    ├── user
    |   ├── controller
    |   ├── dto
    |   ├── model
    |   ├── repository
    |   └── service
└── infra
    ├── querydsl
    └── swagger
```

## 결과 화면
(준비중...)
