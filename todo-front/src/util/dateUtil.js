export const dateFormat = (date) => {
  const dateFormat = new Date(date);
  return `${dateFormat.getFullYear()}-${
    dateFormat.getMonth() + 1 < 10
      ? "0" + (dateFormat.getMonth() + 1)
      : dateFormat.getMonth() + 1
  }-${
    dateFormat.getDate() < 10
      ? "0" + dateFormat.getDate()
      : dateFormat.getDate()
  }`;
};
