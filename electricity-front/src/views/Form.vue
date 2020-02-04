<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-row>
    <v-btn
      name="loadData"
      small
      left
      color="primary"
      v-on:click="submitData"
      dark
      :disabled="appState.nav.isLoading"
      class="mt-4 ml-4"
      ><v-icon>refresh</v-icon>
    </v-btn>
    <v-col cols="12" md="2">
      <v-menu
        v-model="datepicker.starDate.menu"
        :close-on-content-click="false"
        :nudge-right="40"
        transition="scale-transition"
        offset-y
        min-width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="appState.requestData.startDate"
            label="Start date"
            prepend-icon="event"
            readonly
            name="startDate"
            :rules="dateRule.rules"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="appState.requestData.startDate"
          @input="datepicker.starDate.menu = false"
          v-on:change="formChange"
        ></v-date-picker>
      </v-menu>
    </v-col>
    <v-col cols="12" md="2">
      <v-menu
        v-model="datepicker.endDate.menu"
        :close-on-content-click="false"
        :nudge-right="40"
        transition="scale-transition"
        offset-y
        min-width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="appState.requestData.endDate"
            label="End date"
            prepend-icon="event"
            readonly
            name="endDate"
            :rules="dateRule.rules"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="appState.requestData.endDate"
          @input="datepicker.endDate.menu = false"
          v-on:change="formChange"
        ></v-date-picker>
      </v-menu>
    </v-col>
    <v-col cols="12" md="2">
      <v-select
        :items="appState.aggregations"
        v-model="appState.requestData.aggregation"
        label="Aggregation"
        prepend-icon="list"
        v-on:change="formChange"
      ></v-select>
    </v-col>
    <v-col cols="12" md="1"> </v-col>
    <v-col cols="12" md="2">
      <v-text-field
        label="Price"
        value="0.00"
        :rules="priceRule.rules"
        v-bind:suffix="appState.historyData.measurementUnit"
        v-model="appState.requestData.price"
        prepend-icon="attach_money"
      ></v-text-field>
    </v-col>
    <v-btn
      name="calculate"
      small
      color="primary"
      v-on:click="submitData"
      dark
      :disabled="appState.nav.isLoading"
      class="mt-4 ml-4"
      >Calculate Cost
    </v-btn>
  </v-row>
</template>

<style scoped>
label {
  alignment: center;
}
</style>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import { Action, State } from "vuex-class";
import { AppState, RequestData } from "@/store/store";
import moment from "moment";

@Component
export default class Form extends Vue {
  @State("historyData") appState?: AppState;
  @Action("getHistoryData") getData?: any;
  @Action("chartUpdate") chartUpdate?: any;
  panel: any;
  datepicker: any;
  priceRule: { rules: any[] };
  dateRule: { rules: any[] };

  constructor() {
    super();
    this.panel = [0, 1];
    this.datepicker = {
      starDate: {
        menu: false,
        modal: false
      },
      endDate: {
        menu: false,
        modal: false
      }
    };
    this.priceRule = {
      rules: [(value: any) => !isNaN(value) || "This should be number."]
    };
    this.dateRule = {
      rules: [
        (value: any) =>
          moment(value).isBefore(moment(new Date()).add(1, "hour")) ||
          "Date cant be from future.",
        (value: any) =>
          moment(value).isAfter(
            moment(new Date())
              .subtract(2, "year")
              .subtract(1, "day")
          ) ||
          `Date cant be older than ${moment(new Date())
            .subtract(2, "year")
            .subtract(1, "day")}.`
      ]
    };
  }

  mounted() {
    this.submitData();
  }

  formChange() {
    if (this.appState) {
      if (!validate(this.appState.requestData)) {
        return;
      }
    }
    this.chartUpdate();
  }

  submitData() {
    if (this.appState) {
      if (!this.appState.requestData.endDate) {
        this.appState.requestData.endDate = moment(new Date()).format(
          "YYYY-MM-DD"
        );
      }
      if (!this.appState.requestData.startDate) {
        this.appState.requestData.startDate = moment(new Date())
          .subtract(2, "year")
          .format("YYYY-MM-DD");
      }
      if (!validate(this.appState.requestData)) {
        return;
      }
    }
    this.getData();
  }
}

export const validate = (requestData: RequestData) => {
  if (requestData) {
    if (new Date(requestData.startDate) > new Date(requestData.endDate)) {
      showError("Start date cannot be greater than end date.");
      return false;
    }
    if (moment(requestData.startDate) > moment(new Date()).add(1, "day")) {
      return false;
    }
    if (moment(requestData.endDate) > moment(new Date()).add(1, "day")) {
      return false;
    }
    if (
      moment(requestData.startDate) <
      moment(new Date())
        .subtract(2, "year")
        .subtract(1, "day")
    ) {
      return false;
    }
    if (isNaN(requestData.price)) {
      return false;
    }
  }
  return true;
};

export const showError = (error: string) => {
  Vue.toasted.show(error, {
    position: "bottom-left",
    type: "error",
    duration: 5000,
    theme: "bubble"
  });
};
</script>
