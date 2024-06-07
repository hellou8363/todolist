import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { getAccessToken, getJwtToken } from "../api/kakaoApi";

const KakaoRedirectPage = () => {
  const [searchParams] = useSearchParams();
  const authCode = searchParams.get("code");
  const navigate = useNavigate();

  useEffect(() => {
    getAccessToken(authCode).then((data) => {
      getJwtToken(data.access_token).then((data) => {
        localStorage.setItem("todolist_access_token", data.accessToken);
      });
    });
    navigate("/");
  }, [authCode]);

  return (
    <div>
      <p>Kakao Login Reditect</p>
      <p style={{ display: "none" }}>{authCode}</p>
    </div>
  );
};

export default KakaoRedirectPage;
