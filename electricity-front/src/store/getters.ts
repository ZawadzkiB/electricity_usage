import { GetterTree } from "vuex";
import { AppState, Consumption, RequestData, RootState } from "@/store/store";
import moment from "moment";

export const getters: GetterTree<AppState, RootState> = {
  getHistoryDataFromState(state) {
    const historyData = state.historyData.consumptionHistory;
    switch (state.requestData.aggregation) {
      case "daily":
        return filterDates(historyData.DAILY, state.requestData);

      case "weekly":
        return filterDates(historyData.WEEKLY, state.requestData);

      case "monthly":
        return filterDates(historyData.MONTHLY, state.requestData);
    }
  }
};

export const filterDates = (
  consumptions: Consumption[],
  requestData: RequestData
) => {
  return consumptions.filter(
    c =>
      moment(c.timeStamp).isBefore(moment(requestData.endDate).add(1, "day")) &&
      moment(c.timeStamp).isAfter(
        moment(requestData.startDate).subtract(1, "day")
      )
  );
};
