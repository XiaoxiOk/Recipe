import axios from 'axios'
import router from '../router/index'
import { Message } from "element-ui";
import { Notification } from 'element-ui';
import { getToken } from '@/utils/auth';


// create an axios instance
axios.defaults.withCredentials = false
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
const service = axios.create({
  baseURL: 'http://localhost:8099', // api 的 base_url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // 是否需要设置 token

    const isToken = (config.headers || {}).isToken === false
    if (getToken() && !isToken) {
      config.headers['SaToken'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }

    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)


// Add a response interceptor
service.interceptors.response.use(function (response) {

  if (response.data.status === 200) {
    if (response.data.data === "成功") {
      Message.success({
        message: response.data.msg
      })
    } else if (response.data.data === "No Nodes") {

    }
  } else if (response.data.status === 500) {
    Message.error({
      message: response.data.msg
    })
  }
  else if (response.data.status === 300) {
    Message.warning({
      message: response.data.msg
    })
  } else if (response.data.status === 504) {
    Message.warning({
      message: response.data.msg
    })
  } else if (response.data.status === 501 || response.data.status === 502 || response.data.status === 503) {
    Notification.warning({
      title: '嘿！',
      message: response.data.msg,
      position: 'bottom-right'
    });
    //登录信息过期，回到登录页面重新登录
    router.replace({
      path: '/login',
      query: {
        redirect: router.currentRoute.fullPath
      }
    });
  }
  else { //else?
    Notification.warning({
      title: '嘿！',
      message: response.data.msg,
      position: 'bottom-right'
    });
  }
  return response.data
}, function (error) {

  // Do something with response error
  let msg = error.msg
  if (error.response.status === 401) {
    msg = '出错了。'

  } else if (error.response.status === 404) {
    msg = '资源请求找不到'

  }
  Message.error({
    message: '响应错误：' + msg
  })
  return Promise.reject(error)
})

export default service
