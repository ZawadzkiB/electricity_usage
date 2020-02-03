import { MutationTree } from "vuex";
import { HistoryData, AppState } from "@/store/store";

export const mutations: MutationTree<AppState> = {
  setHistoryData(state, historyData: HistoryData) {
    historyData.aggregationType = historyData.aggregationType.toLowerCase();
    state.historyData = historyData;
  },
  dataLoad(state, isLoading: boolean) {
    state.nav.isLoading = isLoading;
  },
  changeView(state) {
    state.nav.view = !state.nav.view;
  }
};
