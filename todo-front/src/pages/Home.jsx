import { useEffect, useRef, useState } from "react";
import { useInView } from "react-intersection-observer";
import Card from "../components/Card";
import "./Home.css";

const Home = () => {
  const [todos, setTodos] = useState([]);
  const [page, setPage] = useState(0);
  const totalCount = useRef(1);
  const [ref, inView] = useInView();

  useEffect(() => {
    if (inView && todos.length < totalCount.current) {
      console.log("todos.length: ", todos.length, ", totalCount: ", totalCount);
      todoFetch();
    }
  }, [inView]);

  const todoFetch = () => {
    fetch(`http://localhost:8080/todos?page=${page}&size=10`)
      .then((res) => res.json())
      .then((data) => {
        setTodos([...todos, ...data.content]);
        setPage((page) => page + 1);
        totalCount.current = data.totalElements;
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <>
      <div className="container">
        {todos.map((value) => {
          return (
            <Card
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
