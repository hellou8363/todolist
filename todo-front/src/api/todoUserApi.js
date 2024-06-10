import { BASE_URI } from "./apiKey";

export const defaultLogin = (email, password) => {
  const header = {
    "Content-Type": "application/json",
  };

  const formBody = {
    email: email,
    password: password,
  };

  return fetch(`${BASE_URI}/users/signin`, {
    method: "POST",
    headers: header,
    credentials: "include",
    body: JSON.stringify(formBody),
  })
    .then((res) => {
      if (!res.ok) {
        const error = (res && res.message) || res.status;
        return Promise.reject(error);
      }

      return res.json();
    })
    .catch((err) => console.log(err.json()));
};

export const defaultSignup = (email, nickname, password) => {
  const header = {
    "Content-Type": "application/json",
  };

  const formBody = {
    email: email,
    nickname: nickname,
    password: password,
  };

  return fetch(`${BASE_URI}/users/signup`, {
    method: "POST",
    headers: header,
    body: JSON.stringify(formBody),
  })
    .then((res) => {
      if (!res.ok) {
        const error = (res && res.message) || res.status;
        return Promise.reject(error);
      }

      return res.json();
    })
    .catch((err) => console.log(err.json()));
};

export const logout = () => {
  fetch(`${BASE_URI}/users/logout`, {
    headers: {
      "Content-Type": "application/json",
      "userId": localStorage.getItem("todolist_user_id")
    },
  })
    .then((res) => {
      if (!res.ok) {
        console.log(res);
      }

      if (res.status === 204) {
        console.log("204");
      }
    })
    .catch((err) => console.log(err));
    
  localStorage.removeItem("todolist_access_token");
  localStorage.removeItem("todolist_user_id");
};
