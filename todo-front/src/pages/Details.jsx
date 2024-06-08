import { useLocation } from "react-router-dom";
import "./Details.css";

const Details = () => {
  const userId = Number(localStorage.getItem("todolist_user_id"));
  const location = useLocation();
  const todo = { ...location.state };
  const date = new Date(todo.createdAt);
  const createDate = `${date.getFullYear()}-${
    date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()
  }-${date.getDay() < 10 ? "0" + date.getDay() : date.getDay()}`;

  // TODO: 수정 버튼 이벤트
  //       수정 버튼을 클릭했을 때, 서버로 토큰과 함께 요청을 보냄
  // TODO: 삭제 버튼 이벤트
  //       삭제 버튼을 클릭했을 때, 서버로 토큰과 함께 요청을 보냄

  return (
    <>
      <div className="details-card">
        <div className="info_wrap">
          <p className="title">{todo.title}</p>
          <p className="writer">{todo.writer}</p>
        </div>
        <p className="date">{createDate}</p>
        <p className="content">{todo.content}</p>
        {todo.userId === userId && (
          <div className="bottom-menu">
            <button>수정</button>
            <button>삭제</button>
          </div>
        )}
      </div>
    </>
  );
};

export default Details;
