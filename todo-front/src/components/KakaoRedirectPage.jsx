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
        localStorage.setItem("todolist_user_id", data.userId); // Todo 상세 페이지에서 수정, 삭제 버튼 활성/비활성화를 위함
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
