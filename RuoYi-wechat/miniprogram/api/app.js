// 导入封装的 网络请求模块实例
import http from '../utils/http'

export const reqLogin = (code) => {
  return http.get(`/wechat/login/${code}`)
}