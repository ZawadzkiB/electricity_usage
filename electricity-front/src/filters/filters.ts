import moment from 'moment'
import Vue from 'vue'

Vue.filter('formatDate', function(value: string) {
    if (value) {
        return moment(String(value)).format('YYYY-MM-DD')
    }
})

Vue.filter('formatCost', function(value: number) {
    if (value) {
        return parseFloat(value.toFixed(2));
    }
})