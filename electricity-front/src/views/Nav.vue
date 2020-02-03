<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-app-bar
    color="indigo darken-2"
    dark
    shrink-on-scroll
    prominent
    max-height="90"
  >
    <Form />
  </v-app-bar>
</template>

<script lang="ts">
import { Component } from "vue-property-decorator";
import Vue from "vue";
import Form from "@/views/Form.vue";
import { State } from "vuex-class";
import { AppState } from "@/store/store";

@Component({
  components: {
    Form
  }
})
export default class Nav extends Vue {
  @State("historyData") appState?: AppState;
}
</script>
