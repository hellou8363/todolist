import { useLocation, useNavigate, useParams } from "react-router-dom";
import "./Details.css";

const Details = () => {
  const userId = Number(localStorage.getItem("todolist_user_id"));
  const todoId = useParams().id;
  const location = useLocation();
  const navigate = useNavigate();
  const todo = { ...location.state };
  const date = new Date(todo.createdAt);
  const createDate = `${date.getFullYear()}-${
    date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()
  }-${date.getDay() < 10 ? "0" + date.getDay() : date.getDay()}`;

  // TODO: 삭제 버튼 이벤트

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
            <button onClick={() => {
              navigate(`/todos`, {
                state: {
                  todoId: todoId,
                  title: todo.title,
                  writer: todo.writer,
                  content: todo.content
                }
              })
            }}>수정</button>
            <button>삭제</button>
          </div>
        )}
      </div>
    </>
  );
};

export default Details;
