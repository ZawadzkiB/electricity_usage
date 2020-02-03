import Vue from "vue";
import Vuex, { StoreOptions } from "vuex";
import { historyData } from "@/store/index";

Vue.use(Vuex);

const store: StoreOptions<RootState> = {
  state: {
    version: "1.0.0"
  },
  modules: {
    historyData
  }
};

export default new Vuex.Store(store);

export interface RootState {
  version: string;
}

export interface AggregatedConsumptions {
  DAILY: Array<Consumption>;
  WEEKLY: Array<Consumption>;
  MONTHLY: Array<Consumption>;
}

export interface HistoryData {
  documentIdentification: string;
  documentDateTime: string;
  accountingPoint: string;
  measurementUnit: string;
  consumptionHistory: AggregatedConsumptions;
}

export interface Consumption {
  timeStamp: string;
  consumption: number;
  cost: number;
}

export interface AppState {
  historyData: HistoryData;
  aggregations: Array<Options>;
  requestData: RequestData;
  nav: NavigationOptions;
}

export interface RequestData {
  aggregation: string;
  startDate: string;
  endDate: string;
  price: number;
}

export interface Options {
  value: string;
  text: string;
}

export interface NavigationOptions {
  view: boolean;
  isLoading: boolean;
  selected: string;
}
