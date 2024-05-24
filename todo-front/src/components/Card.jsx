import "./Card.css";
import PropTypes from "prop-types";

const Card = ({ title, writer, content, createdAt }) => {
  const date = new Date(createdAt);
  const createDate = `${date.getFullYear()}-${
    date.getMonth() < 10 ? "0" + date.getMonth() : date.getMonth()
  }-${date.getDay() < 10 ? "0" + date.getDay() : date.getDay()}`;

  return (
    <div className="card">
      <p className="title">{title}</p>
      <p className="writer">{writer}</p>
      <p className="content">{content}</p>
      <p className="date">{createDate}</p>
    </div>
  );
};

Card.propTypes = {
  title: PropTypes.string,
  writer: PropTypes.string,
  content: PropTypes.string,
  createdAt: PropTypes.string,
};

export default Card;
