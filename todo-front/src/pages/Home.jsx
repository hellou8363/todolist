import { useEffect, useState } from "react";
import Card from "../components/Card";
import "./Home.css";

const Home = () => {
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/todos")
      .then((res) => res.json())
      .then((data) => setTodos([...todos, ...data.content]))
      .catch((err) => console.log(err.message));
  }, []);

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
      </div>
    </>
  );
};

export default Home;
