import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style



NProgress.configure({ showSpinner: false })// NProgress Configuration


const whiteList = ['/login']// no redirect whitelist


router.beforeEach((to, from, next) => {

    next()


})

router.afterEach(() => {

})
