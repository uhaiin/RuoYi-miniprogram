// 导入封装的 网络请求模块实例
import http from '../utils/http'

export const reqIndexData = () => {
  return http.all(
    http.get('/wechat/test')
  )
}

export const importCode = (brandId, brand, packageId, packageName, code, remark) => {
  return http.post(`/api/wechat/code/import`, {
    brandId,
    brand,
    packageId,
    packageName,
    code,
    remark
  })
}