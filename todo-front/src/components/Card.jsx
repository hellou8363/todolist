import "./Card.css";
import PropTypes from "prop-types";
import { dateFormat } from "../util/dateUtil";

const Card = ({ title, writer, content, createdAt, onClick }) => {
  const createDate = dateFormat(createdAt);

  return (
    <div className="card" onClick={onClick}>
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
  onClick: PropTypes.func,
};

export default Card;
