<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-app-bar color="indigo darken-2" dark shrink-on-scroll prominent>
    <Form />
    <v-btn
      color="blue"
      fab
      dark
      large
      absolute
      top
      right
      class="mr-4 mt-12"
      @click="switchToView()"
    >
      <v-icon v-if="!appState.nav.view">show_chart</v-icon>
      <v-icon v-else>table_chart</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script lang="ts">
import { Component } from "vue-property-decorator";
import Vue from "vue";
import Form from "@/views/Form.vue";
import { Action, State } from "vuex-class";
import { AppState } from "@/store/store";

@Component({
  components: {
    Form
  }
})
export default class Nav extends Vue {
  @State("historyData") appState?: AppState;
  @Action("switchView") switchToView: any;
}
</script>
