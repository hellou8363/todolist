import { useEffect, useRef, useState } from "react";
import { useInView } from "react-intersection-observer";
import Card from "../components/Card";
import "./Home.css";
import { useNavigate } from "react-router-dom";
import { getTodoList } from "../api/todoApi";

const Home = () => {
  const [todos, setTodos] = useState([]);
  const [page, setPage] = useState(0);
  const size = 10;
  const totalCount = useRef(1);
  const [ref, inView] = useInView();
  const navigate = useNavigate();

  useEffect(() => {
    if (inView && todos.length < totalCount.current) {
      getTodoList(page, size).then((data) => {
        setTodos([...todos, ...data.content]);
        setPage((page) => page + 1);
        totalCount.current = data.totalElements;
      });
    }
  }, [inView]);

  return (
    <>
      <div className="container">
        {todos.map((value) => {
          return (
            <Card
              onClick={() => {
                navigate(`/todos/${value.id}`, {
                  state: {
                    title: value.title,
                    content: value.content,
                    userId: value.userId,
                    writer: value.writer,
                    createdAt: value.createdAt,
                  },
                });
              }}
              key={value.id}
              title={value.title}
              writer={value.writer}
              content={value.content}
              createdAt={value.createdAt}
            />
          );
        })}
        <div ref={ref}></div>
      </div>
    </>
  );
};

export default Home;
