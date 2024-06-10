## 목차
+ [프로젝트 소개](#프로젝트-소개)
+ [프로젝트 기간](#프로젝트-기간)
+ [개발 도구 및 환경](#개발-도구-및-환경)
+ [Use Case Diagram](#use-case-diagram)
+ [ERD](#erd)
+ [API 명세서](#api-명세서)
+ [테이블 생성](#테이블-생성)
+ [패키지 구조](#패키지-구조)
+ [결과 화면](#결과-화면)
+ [코드 설명](#코드-설명)

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
### 회원가입 및 로그인 화면
![todolist_회원가입및로그인화면](https://github.com/hellou8363/todolist/assets/89592727/7c01d0a5-93f4-4101-a943-1419019fdea3)

## 코드 설명
<details>
    <summary><b>소셜 회원가입 및 로그인 구현</b></summary>
    <div markdown="1">
Client로부터 Kakao의 AccessToken을 전달 받는다.  

```kotlin
@GetMapping("/signin/kakao")
fun signinKakao(@RequestParam accessToken: String, response: HttpServletResponse) { 
    userService.signWithKakao(accessToken, response)
}
```

Service에서 진행되는 로직은 다음과 같다.
1. AccessToken으로 카카오에서 회원 정보를 가져온다.
2. Database에 카카오 회원 정보의 이메일과 일치하는 이메일이 있는지 확인한다.  
    2-1. 일치하는 이메일이 있다면 예외 발생 없이 userInfo 변수에 해당 회원의 정보가 담긴다.  
    2-2. 일치하는 이메일이 없다면 EmptyResultDataAccessException 예외가 발생하며 5번을 수행한다.
3. 가져온 회원 정보의 이메일이 이미 카카오로 가입된 회원인지 확인한다.  
    3-1. 이미 카카오로 가입된 회원이면 로그인으로 처리한다.  
    3-2. 카카오로 가입된 회원이 아니라면 일반회원(default)으로 가입된 회원이므로 가입종류에 "KAKAO"를 추가한 후 로그인 처리한다.  
4. EmptyResultDataAccessException 예외가 발생했다는 것은 DB에 이메일과 일치하는 회원 정보가 없기 때문으로 신규가입 처리한다.  
    4-1. 신규회원으로 등한다.  
    4-2. 신규회원으로 등록된 정보로 로그인 처리한다.

```kotlin
@Transactional
override fun signWithKakao(accessToken: String, response: HttpServletResponse) {

    // 1. 카카오에서 회원 정보를 가져옴
    val kakaoUserInfo = getUserFromKakao(accessToken)

    try {
        // 2. DB에 가져온 회원 정보의 이메일이 존재하는지 확인
        val userInfo = userRepository.findByEmail(kakaoUserInfo["email"] as String)

         // 3. 이미 카카오로 가입된 회원인지 확인
        if (userInfo.joinType.contains("KAKAO")) {

            // 3-1. 로그인 처리
            return signinUser(
                SigninRequest(
                    kakaoUserInfo["email"] as String,
                    "null"
                ),
                response
            )
        }

        // 3-2. 가입종류에 KAKAO 추가 후 로그인 처리
        // 2번에서 예외가 발생하지 않는 것은 회원 정보가 존재하기 때문
        // 회원가입의 기본 jointype은 "EMAIL"
        userInfo.joinType = "${userInfo.joinType}, KAKAO"

        return signinUser(
            SigninRequest(
                kakaoUserInfo["email"] as String,
                   "null"
            ),
            response
        )

    // 4. EmptyResultDataAccessException 예외가 발생했다는 것은
    // DB에 이메일과 일치하는 회원 정보가 없기 때문
    } catch (e: EmptyResultDataAccessException) { 

        // 4-1. 신규회원으로 등록
        createUser(
            CreateUserRequest(
                kakaoUserInfo["email"].toString(),
                kakaoUserInfo["nickname"].toString(),
                "null"
            ),
            joinType = "KAKAO"
        )

        // 4-2. 신규회원으로 등록된 정보로 로그인 처리
        return signinUser(
            SigninRequest(
                kakaoUserInfo["email"] as String,
                 "null"
            ),
           response
        )
    }
}
```

</div></details>
<details>
    <summary><b>소셜 로그인 회원 가입 처리 시 비밀번호를 "null"로 설정함으로 인해 일반 가입을 하지 않은 회원이 소셜 이메일로 일반 로그인을 시도할 수 있지 않은가?</b></summary><div>
해당 부분은 UserDetailsService 구현 클래스의 loadUserByUsername에서 사용자 정보를 불러온 후 가입종류(jointype)에 기본 가입 종류인 "EMAIL"이 없다면 401 Unauthorized이 발생되도록 처리했다.

```kotlin
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        var user: User

        try {
            user = userRepository.findByEmail(email)

            if(!user.joinType.contains("EMAIL")) {
                throw UsernameNotFoundException(email)
            }

        } catch (e: InternalAuthenticationServiceException) {
            throw UsernameNotFoundException(email)
        }

        return CustomUserDetails(user)
    }
}
```
