import { BASE_URI } from "./apiKey";

export const getTodoList = async (page, size) => {
  return fetch(`${BASE_URI}/todos?page=${page}&size=${size}`)
    .then((res) => {
      if (!res.ok) {
        console.log(res);
      }
      return res.json();
    })
    .catch((err) => console.log(err));
};

export const createTodo = (title, writer, content) => {
  const header = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("todolist_access_token")}`,
  };

  const formBody = {
    title: title,
    writer: writer,
    content: content,
  };

  return fetch(`${BASE_URI}/todos`, {
    method: "POST",
    headers: header,
    body: JSON.stringify(formBody),
  })
    .then((res) => {
      if (!res.ok) {
        console.log(res);
      }
      return res.json();
    })
    .catch((err) => console.log(err));
};

export const updateTodo = (todoId, title, writer, content) => {
  const header = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("todolist_access_token")}`,
  };

  const formBody = {
    title: title,
    writer: writer,
    content: content,
  };

  return fetch(`${BASE_URI}/todos/${todoId}`, {
    method: "PUT",
    headers: header,
    body: JSON.stringify(formBody),
  })
    .then((res) => {
      if (!res.ok) {
        console.log(res);
      }
      return res.json();
    })
    .catch((err) => console.log(err));
};

export const deleteTodo = (todoId) => {
  fetch(`${BASE_URI}/todos/${todoId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("todolist_access_token")}`,
    },
  })
    .then((res) => {
      if (!res.ok) {
        console.log(res);
      }

      if (res.status === 204) {
        console.log("204");
      }
    })
    .catch((err) => console.log(err));
};
