import {
  observable,
  action
} from 'mobx-miniprogram'
import {
  getStorage
} from '../utils/storage'

export const userStore = observable({
  // token 身份令牌
  token: getStorage('token') || '',

  // 用户信息
  userInfo: getStorage('userInfo') || {},

  // setToken 用来修改、更新 token
  setToken: action(function (token) {
    // 在调用 setToken 方法时，需要传入 token 数据进行赋值
    this.token = token
  }),

  // 对用户信息进行赋值
  setUserInfo: action(function (userInfo) {
    this.userInfo = userInfo
  })
})