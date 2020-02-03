import { ActionTree } from "vuex";
import { AppState, RootState } from "@/store/store";
import { CLIENT, SHOW_ERROR } from "@/api/config";
import Vue from "vue";

export const actions: ActionTree<AppState, RootState> = {
  async getHistoryData({ commit, state }) {
    commit("dataLoad", true);
    await CLIENT.get(
      `/report?price=${state.requestData.price}`
    ).then(
      response => {
        Vue.toasted.show("Loaded new data", {
          position: "bottom-left",
          type: "info",
          duration: 3000,
          theme: "bubble"
        });
        commit("setHistoryData", response.data);
        commit("dataLoad", false);
      },
      error => {
        SHOW_ERROR(error);
        commit("dataLoad", false);
      }
    );
  },
  switchView({ commit }) {
    commit("changeView");
  },
  chartUpdate({ commit }) {
    commit("chartUpdate");
  }
};
