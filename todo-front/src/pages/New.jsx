import { useRef } from "react";
import { BASE_URI } from "../api/apiKey";
import "./New.css";
import { useLocation, useNavigate } from "react-router-dom";
import { createTodo, updateTodo } from "../api/todoApi";

const New = () => {
  const location = useLocation();
  const todo = { ...location.state };
  const isNew = Object.keys(todo).length === 0;
  const titleRef = useRef();
  const writerRef = useRef();
  const contentRef = useRef();
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    event.preventDefault();

    if (isNew) {
      createTodo(
        titleRef.current.value,
        writerRef.current.value,
        contentRef.current.value
      ).then(() => {
        navigate("/");
      });
    } else {
      updateTodo(
        todo.todoId,
        titleRef.current.value,
        writerRef.current.value,
        contentRef.current.value
      ).then(() => {
        navigate("/");
      });
    }
  };

  return (
    <div className="create-container">
      <form
        className="create-form"
        action={`${BASE_URI}/todos`}
        method="post"
        onSubmit={handleSubmit}
      >
        <label>
          Title:
          <br />
          <input
            type="text"
            ref={titleRef}
            min={1}
            max={200}
            defaultValue={todo.title ? todo.title : null}
            required
          />
        </label>
        <label>
          Writer:
          <br />
          <input
            type="text"
            ref={writerRef}
            minLength={1}
            maxLength={1000}
            defaultValue={todo.writer ? todo.writer : null}
            required
          />
        </label>
        <label>Content:</label>
        <textarea
          rows={12}
          ref={contentRef}
          minLength={1}
          maxLength={1000}
          defaultValue={todo.content ? todo.content : null}
        ></textarea>
        <button type="submit">{isNew ? "등록" : "수정"}</button>
      </form>
    </div>
  );
};

export default New;
