import { Route, Routes } from "react-router-dom";
import "./App.css";
import Home from "./pages/Home";
import Details from "./pages/Details";
import New from "./pages/New";
import Notfound from "./pages/Notfound";
import Login from "./pages/Signin";
import Signup from "./pages/Signup";
import KakaoRedirectPage from "./components/KakaoRedirectPage";
import NaverRedirectPage from "./components/NaverRedirectPage";
import TokenRefreshPage from "./components/TokenRefreshPage";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/todos/:id" element={<Details />} />
        <Route path="/todos" element={<New />} />
        <Route path="/users/signup" element={<Signup />} />
        <Route path="/users/signin" element={<Login />} />
        <Route path="/users/kakao" element={<KakaoRedirectPage />} />
        <Route path="/users/naver" element={<NaverRedirectPage />} />
        <Route path="/users/refresh" element={<TokenRefreshPage />} />
        <Route path="*" element={<Notfound />} />
      </Routes>
    </>
  );
}
