import {
  KAKAO_AUTH_CODE_PATH,
  KAKAO_REST_API_KEY,
  KAKAO_REDIRECT_URI,
  KAKAO_ACCESS_TOKEN_URI,
  KAKAO_CLIENT_SECRET,
  BASE_URI,
} from "./apiKey";

export const getKakaoLoginLink = () =>
  `${KAKAO_AUTH_CODE_PATH}?response_type=code&client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}`;

export const getAccessToken = async (authCode) => {
  const header = {
    "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
  };

  const details = {
    grant_type: "authorization_code",
    client_id: KAKAO_REST_API_KEY,
    redirect_uri: KAKAO_REDIRECT_URI,
    code: authCode,
    client_secret: KAKAO_CLIENT_SECRET,
  };

  const formBody = Object.keys(details)
    .map(
      (key) => `${encodeURIComponent(key)}=${encodeURIComponent(details[key])}`
    )
    .join("&");

  return fetch(KAKAO_ACCESS_TOKEN_URI, {
    method: "POST",
    headers: header,
    body: formBody,
  })
    .then((res) => {
      if (!res.ok) {
        const errorMessage = res.json();
        console.log("errorMessage: ", errorMessage);
      }

      return res.json();
    })
    .catch((err) => console.log(err));
};

export const getJwtToken = async (accessToken) => {
  return fetch(`${BASE_URI}/users/signin/kakao?accessToken=${accessToken}`, {
    method: "GET",
    credentials: "include",
  })
    .then((res) => {
      if (!res.ok) {
        const errorMessage = res.json();
        console.log("errorMessage: ", errorMessage);
      }

      return res.json();
    })
    .catch((err) => console.error(err));
};
