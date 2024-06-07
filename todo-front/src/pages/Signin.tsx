import "./Signin.css";
import { getKakaoLoginLink } from "../api/kakaoApi";
import { getNaverLoginLink } from "../api/naverApi";
import { Link, useNavigate } from "react-router-dom";
import { useRef } from "react";
import { defaultLogin } from "../api/todoUserApi";
import React from "react";
import { BASE_URI } from "../api/apiKey";

const Login = () => {
  const kakaoLoginLink = getKakaoLoginLink();
  const naverLoginLink = getNaverLoginLink();
  const emailRef = useRef();
  const passwordRef = useRef();
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    event.preventDefault();

    defaultLogin(emailRef.current.value, passwordRef.current.value).then(() => {
      navigate("/");
    });
  };

  return (
    <>
      <div className="login-container">
        <img
          className="logo"
          src="../../main-img.jpg"
          alt="todolist를 작성하는 리트리버"
        />
        <form
          className="login-form"
          action={`${BASE_URI}/users/signin`}
          method="post"
          onSubmit={handleSubmit}
        >
          <label>
            ID:
            <input type="text" ref={emailRef} />
          </label>
          <label>
            Pw:
            <input type="password" ref={passwordRef} />
          </label>
          <button type="submit">로그인</button>
        </form>
        <Link to={kakaoLoginLink}>
          <img src="../../kakao.png" alt="카카오 로그인 버튼" />
        </Link>
        <Link to={naverLoginLink}>
          <img src="../../naver.png" alt="네이버 로그인 버튼" />
        </Link>
      </div>
    </>
  );
};

export default Login;
