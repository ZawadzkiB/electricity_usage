import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "@/store/store";
import "bootstrap/dist/css/bootstrap.css";
import "material-design-icons-iconfont/dist/material-design-icons.css";

import Toasted from "vue-toasted";
import vuetify from "./plugins/vuetify";

Vue.config.productionTip = false;

Vue.use(Toasted);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
