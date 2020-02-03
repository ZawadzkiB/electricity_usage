<template>
  <div class="dashboard">
    <Nav style="display: block" />
    <DataTable v-if="!appState.nav.view"></DataTable>
    <Chart :height="this.height" v-else></Chart>
  </div>
</template>

<script lang="ts">
import { Component, Prop } from "vue-property-decorator";
import Vue from "vue";
import Nav from "@/views/Nav.vue";
import DataTable from "@/views/DataTable.vue";
import Chart from "@/views/Chart.vue";
import { State } from "vuex-class";
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
  height: number;
  constructor() {
    super();
    this.height = window.innerHeight * 0.8;
  }
}
</script>
