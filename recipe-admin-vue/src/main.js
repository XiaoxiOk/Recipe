import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import './icons' // icon
import './permission' // permission control

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss'
//时间格式化工具
import moment from 'moment'
Vue.prototype.$moment = moment

Vue.config.productionTip = false
Vue.use(Element, {
  size: sessionStorage.getItem('size') || 'medium' // set element-ui default size
}) // global css

store.dispatch('GenerateRoutes',).then(() => { // 根据perms权限生成可访问的路由表
  router.addRoutes(store.getters.addRoutes) // 动态添加可访问路由表
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
