import { Module } from "vuex";
import { RootState, AppState } from "@/store/store";
import { getters } from "@/store/getters";
import { actions } from "@/store/actions";
import { mutations } from "@/store/mutations";

export const state: AppState = {
  aggregations: [
    { value: "daily", text: "DAILY" },
    { value: "weekly", text: "WEEKLY" },
    { value: "monthly", text: "MONTHLY" }
  ],
  nav: {
    view: false,
    isLoading: false,
    selected: "data"
  },
  requestData: {
    aggregation: "daily",
    startDate: "",
    endDate: "",
    price: 0
  },
  historyData: {
    documentIdentification: "1580398712",
    documentDateTime: "2020-01-30T17:38:32",
    accountingPoint: "54EA-5481353548-U",
    measurementUnit: "kWh",
    consumptionHistory: [],
    aggregationType: "daily"
  }
};

export const historyData: Module<AppState, RootState> = {
  state,
  getters,
  actions,
  mutations
};
