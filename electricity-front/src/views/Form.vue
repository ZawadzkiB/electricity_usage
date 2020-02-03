<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-form v-on:submit.prevent="submitData()">
      <v-row>
        <v-col cols="12" md="3">
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
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col cols="12" md="3">
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
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col cols="12" md="3">
          <v-select
            :items="appState.aggregations"
            v-model="appState.requestData.aggregation"
            v-on:change="submitData"
            label="Aggregation"
            prepend-icon="list"
          ></v-select>
        </v-col>
        <v-col cols="12" md="3">
          <v-text-field
            label="Price"
            value="0.00"
            :rules="priceRule.rules"
            v-bind:suffix="appState.historyData.measurementUnit"
            v-model="appState.requestData.price"
            prepend-icon="attach_money"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-btn
        name="loadData"
        absolute
        bottom
        left
        large
        color="primary"
        type="submit"
        dark
        :disabled="appState.nav.isLoading"
        >Load data
      </v-btn>
    </v-form>
  </div>
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
import { AppState } from "@/store/store";
import format from "date-fns/format";
import { addDays, subDays, subYears } from "date-fns";

@Component
export default class Form extends Vue {
  @State("historyData") appState?: AppState;
  @Action("getHistoryData") getData?: any;
  @Action("clearErrors") clearErrorMessage?: any;
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
          new Date(value) < new Date() || "Date cant be from future.",
        (value: any) =>
          new Date(value) > subDays(subYears(new Date(), 2), 1) ||
          `Date cant be older than ${subDays(
            subYears(new Date(), 2),
            1
          ).toDateString()}.`
      ]
    };
  }

  submitData() {
    if (this.appState) {
      let requestData = this.appState.requestData;
      if (!requestData.endDate) {
        requestData.endDate = format(new Date(), "yyyy-MM-dd");
      }
      if (!requestData.startDate) {
        requestData.startDate = format(subYears(new Date(), 2), "yyyy-MM-dd");
      }
      if (new Date(requestData.startDate) > new Date(requestData.endDate)) {
        Form.showError("Start date cannot be greater than end date.");
        return;
      }
      if (new Date(requestData.startDate) > new Date()) {
        Form.showError("Start date cannot be from future.");
        return;
      }
      if (new Date(requestData.endDate) > addDays(new Date(), 1)) {
        Form.showError("End date cannot be from future.");
        return;
      }
      if (
        new Date(requestData.startDate) < subDays(subYears(new Date(), 2), 1)
      ) {
        Form.showError("Start date cannot be older than 2 years.");
        return;
      }
      if (isNaN(requestData.price)) {
        Form.showError("Price should be number.");
        return;
      }
    }
    this.getData();
  }

  private static showError(error: string) {
    Vue.toasted.show(error, {
      position: "bottom-left",
      type: "error",
      duration: 3000,
      theme: "bubble"
    });
  }
}
</script>
