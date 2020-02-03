<template>
  <v-card>
    <v-card-title>
      Electricity usage costs
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        label="Search"
        single-line
        hide-details
        prepend-icon="search"
      ></v-text-field>
    </v-card-title>
    <v-data-table
      v-if="!appState.nav.isLoading"
      :headers="fields2"
      :items="getHistoryDataFromState.consumptionHistory"
      :sort-by="['timeStamp']"
      :sort-desc="[true, false]"
      :search="search"
    ></v-data-table>
    <v-data-table
      v-else
      item-key="name"
      class="elevation-1"
      loading
      loading-text="Loading... Please wait"
    ></v-data-table>
  </v-card>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import { Getter, State } from "vuex-class";
import { AppState, HistoryData } from "@/store/store";

@Component
export default class DocumentsContent extends Vue {
  @State("historyData") appState?: AppState;
  @Getter("getHistoryDataFromState") getHistoryDataFromState?: HistoryData;
  fields2: Array<Fields>;
  search: string;

  constructor() {
    super();
    this.search = "";
    this.fields2 = [
      {
        value: "timeStamp",
        text: "Date",
        sortable: true
      },
      {
        value: "consumption",
        text: "Usage",
        sortable: false
      },
      {
        value: "cost",
        text: "Cost",
        sortable: false
      }
    ];
  }
}

export interface Fields {
  value: string;
  text: string;
  sortable: boolean;
}
</script>
