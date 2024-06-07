import { useLocation } from "react-router-dom";
import "./Details.css";

const Details = () => {
  const location = useLocation();
  const todo = { ...location.state };
  const date = new Date(todo.createdAt);
  const createDate = `${date.getFullYear()}-${
    date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()
  }-${date.getDay() < 10 ? "0" + date.getDay() : date.getDay()}`;

  return (
    <>
      <div className="details-card">
        <div className="info_wrap">
          <p className="title">{todo.title}</p>
          <p className="writer">{todo.writer}</p>
        </div>
        <p className="date">{createDate}</p>
        <p className="content">{todo.content}</p>
      </div>
    </>
  );
};

export default Details;
