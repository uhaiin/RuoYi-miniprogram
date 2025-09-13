// 导入封装的网络请求模块实例
import http from '../utils/http'

/**
 * @description 获取商品分类的数据
 * @returns Promise
 */
export const reqCategoryData = () => {
  return http.get('/api/wechat/index/category/list')
}

/**
 * 获取二级分类信息
 * @param {*} categoryId 二级分类的父 id
 */
export const getSubCategoryData = (categoryId) => {
  return http.get(`/api/wechat/index/category/list/${categoryId}`)
}

/**
 * 获取具体二级分类下的商品信息
 * @param {*} categoryId 
 */
export const getSubCategoryGoodsData = (categoryId) => {
  return http.get(`/api/wechat/category/getSubCategoryGoodsData/${categoryId}`)
}

/**
 * 获取初始商品信息
 */
export const getInitialGoodsInfo = () => {
  return http.get(`/api/wechat/category/getInitialGoodsInfo`)
}