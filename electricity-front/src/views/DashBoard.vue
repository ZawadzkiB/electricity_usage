<template>
  <div class="dashboard">
    <Nav />
    <v-btn
      color="blue"
      fab
      dark
      large
      absolute
      top
      right
      class="noFocus mr-4 mt-10"
      @click="switchToView()"
    >
      <v-icon v-if="!appState.nav.view">show_chart</v-icon>
      <v-icon v-else>table_chart</v-icon>
    </v-btn>
    <DataTable v-if="!appState.nav.view"></DataTable>
    <Chart :height="this.height" v-else></Chart>
  </div>
</template>

<style scoped>
.noFocus:focus {
  outline: none;
}
</style>

<script lang="ts">
import { Component } from "vue-property-decorator";
import Vue from "vue";
import Nav from "@/views/Nav.vue";
import DataTable from "@/views/DataTable.vue";
import Chart from "@/views/Chart.vue";
import { Action, State } from "vuex-class";
import { AppState } from "@/store/store";

@Component({
  components: {
    Nav,
    DataTable,
    Chart
  }
})
export default class DashBoard extends Vue {
  @State("historyData") appState?: AppState;
  @Action("switchView") switchToView: any;
  height: number;
  constructor() {
    super();
    this.height = window.innerHeight * 0.8;
  }
}
</script>
