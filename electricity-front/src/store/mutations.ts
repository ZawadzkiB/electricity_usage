import { MutationTree } from "vuex";
import { HistoryData, AppState, Consumption } from "@/store/store";
import moment from "moment";

export const mutations: MutationTree<AppState> = {
  setHistoryData(state, historyData: HistoryData) {
    historyData.consumptionHistory.DAILY.forEach(consumption => {
      formatConsumptions(consumption, "YYYY-MM-DD");
    });
    historyData.consumptionHistory.WEEKLY.forEach(consumption => {
      formatConsumptions(consumption, "YYYY-MM-DD");
    });
    historyData.consumptionHistory.MONTHLY.forEach(consumption => {
      formatConsumptions(consumption, "YYYY-MM");
    });
    state.historyData = historyData;
  },
  dataLoad(state, isLoading: boolean) {
    state.nav.isLoading = isLoading;
  },
  changeView(state) {
    state.nav.view = !state.nav.view;
  },
  chartUpdate() {}
};

export const formatConsumptions = (
  consumption: Consumption,
  format: string
) => {
  consumption.cost = parseFloat(consumption.cost.toFixed(2));
  consumption.consumption = parseFloat(consumption.consumption.toFixed(2));
  consumption.timeStamp = moment(String(consumption.timeStamp)).format(format);
};
