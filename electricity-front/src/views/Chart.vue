<script lang="ts">
import { Line } from "vue-chartjs-typescript";
import Vue from "vue";
import { Component } from "vue-property-decorator";
import { Getter, State } from "vuex-class";
import { AppState, Consumption } from "@/store/store";

@Component({
  extends: Line
})
export default class Chart extends Vue {
  @State("historyData") appState?: AppState;
  @Getter("getHistoryDataFromState") getHistoryDataFromState?: any;
  chartData: any;
  options: object;

  constructor() {
    super();
    this.chartData = {
      labels: [],
      datasets: [
        {
          label: "Electricity usage",
          data: [],
          backgroundColor: "rgba(171,205,255, 0)",
          borderColor: "rgb(2,4,255)",
          borderWidth: 1
        },
        {
          label: "Electricity usage cost",
          data: [],
          backgroundColor: "rgba(255,135,134, 0)",
          borderColor: "rgb(255,0,27)",
          borderWidth: 1
        }
      ]
    };

    this.options = {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        duration: 0
      },
      hover: {
        animationDuration: 0
      },
      responsiveAnimationDuration: 0,
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true
            }
          }
        ],
        xAxes: [
          {
            ticks: {
              beginAtZero: true,
              max: 15
            }
          }
        ]
      }
    };
  }

  created() {
    this.$store.subscribe((mutation, state) => {
      if (state.historyData.nav.view && mutation.type == "chartUpdate") {
        this.createChart();
      }
    });
  }

  mounted() {
    this.createChart();
  }

  createChart() {
    this.chartData.labels = this.getHistoryDataFromState.map(
      (c: Consumption) => c.timeStamp
    );

    this.chartData.datasets[0].data = this.getHistoryDataFromState.map(
      (c: Consumption) => c.consumption
    );

    this.chartData.datasets[1].data = this.getHistoryDataFromState.map(
      (c: Consumption) => c.cost
    );

    try {
      this.renderChart(this.chartData, this.options);
      // eslint-disable-next-line no-empty
    } catch (e) {}
  }

  public renderChart!: (chartData: any, options: any) => void;
}
</script>
