import { useLocation, useNavigate, useParams } from "react-router-dom";
import "./Details.css";
import { deleteTodo } from "../api/todoApi";
import { dateFormat } from "../util/dateUtil";

const Details = () => {
  const userId = Number(localStorage.getItem("todolist_user_id"));
  const todoId = useParams().id;
  const location = useLocation();
  const navigate = useNavigate();
  const todo = { ...location.state };
  const createDate = dateFormat(todo.createdAt);

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
            <button
              onClick={() => {
                navigate(`/todos`, {
                  state: {
                    todoId: todoId,
                    title: todo.title,
                    writer: todo.writer,
                    content: todo.content,
                  },
                });
              }}
            >
              수정
            </button>
            <button
              onClick={() => {
                const answer = confirm("정말 삭제하시겠습니까?");

                if (answer) {
                  deleteTodo(todoId);
                  setTimeout(() => {
                    // Http Status 204는 No Content라서 다음과 같이 처리
                    navigate("/");
                  }, 500);
                }
              }}
            >
              삭제
            </button>
          </div>
        )}
      </div>
    </>
  );
};

export default Details;
