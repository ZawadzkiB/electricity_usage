import { GetterTree } from "vuex";
import { AppState, RootState } from "@/store/store";

export const getters: GetterTree<AppState, RootState> = {
  getHistoryDataFromState(state) {
    let historyData = state.historyData;
    if (historyData.aggregationType === "monthly") {
      historyData.consumptionHistory.forEach(consumption => {
        let date = new Date(consumption.timeStamp);
        consumption.timeStamp = `${date.getFullYear()}-${(
          "0" +
          (date.getMonth() + 1)
        ).slice(-2)}`;
      });
    }
    if (
      historyData.aggregationType === "daily" ||
      historyData.aggregationType === "weekly"
    ) {
      historyData.consumptionHistory.forEach(consumption => {
        let date = new Date(consumption.timeStamp);
        consumption.timeStamp = `${date.getFullYear()}-${(
          "0" +
          (date.getMonth() + 1)
        ).slice(-2)}-${("0" + date.getDate()).slice(-2)}`;
      });
    }

    historyData.consumptionHistory.forEach(consumption => {
      consumption.cost = parseFloat(consumption.cost.toFixed(2));
      consumption.consumption = parseFloat(consumption.consumption.toFixed(2));
    });
    return historyData;
  }
};
