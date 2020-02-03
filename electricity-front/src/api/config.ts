import Axios, { AxiosError, AxiosResponse } from "axios";
import Vue from "vue";

export const CLIENT = (() => {
  return Axios.create({
    baseURL:
      process.env.ENV === "docker"
        ? "http://backend:8081"
        : "http://localhost:8081",
    headers: { "Access-Control-Allow-Origin": "*" }
  });
})();

export const SHOW_ERROR = (error: AxiosError<any>) => {
  ERROR_TOAST(
    `Error sending request: ` + error &&
      error.response &&
      error.response.data.message
  );
};

export const ERROR_TOAST = (message: string) => {
  Vue.toasted.show(message, {
    position: "bottom-left",
    type: "error",
    duration: 3000,
    theme: "bubble"
  });
};
