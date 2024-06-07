import React, { useRef } from "react";
import "./Signup.css";
import { BASE_URI } from "../api/apiKey";
import { defaultSignup } from "../api/todoUserApi";
import { useNavigate } from "react-router-dom";

const Signup = () => {
  const emailRef = useRef();
  const nicknameRef = useRef();
  const passwordRef = useRef();
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    event.preventDefault();

    defaultSignup(
      emailRef.current.value,
      nicknameRef.current.value,
      passwordRef.current.value
    ).then(() => {
      navigate("/");
    });
  };

  return (
    <div className="signup-container">
      <img src="../../signup.jpg" />
      <form
        className="signup-form"
        action={`${BASE_URI}/users/signup`}
        method="post"
        onSubmit={handleSubmit}
      >
        <label>
          Email:
          <br />
          <input type="email" ref={emailRef} required />
        </label>
        <label>
          Nickname:
          <br />
          <input
            type="text"
            ref={nicknameRef}
            minLength={4}
            maxLength={10}
            required
          />
        </label>
        <label>
          Password:
          <br />
          <input
            type="password"
            ref={passwordRef}
            minLength={4}
            maxLength={10}
            required
          />
        </label>
        <button type="submit">회원가입</button>
      </form>
    </div>
  );
};

export default Signup;
