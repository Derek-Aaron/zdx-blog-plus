import dayjs from "dayjs";

export function formatDate(date, format = 'YYYY-MM-DD') {
  return dayjs(date).format(format);
}

export function formatDateTime(date, format = "YYYY-MM-DD HH:mm:ss") {
  return formatDate(date, format);
}
