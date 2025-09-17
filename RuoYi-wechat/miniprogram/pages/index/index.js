import {
  reqIndexData
} from '@/api/index';
// 导入本地存储 api
import {
  setStorage
} from '@/utils/storage';
// 导入封装通用模块方法
import {
  toast
} from '@/utils/extendApi'
Page({
  data: {},
  // 监听页面的加载
  onLoad() {
    // 在页面加载以后，调用获取首页数据的方法
    this.getIndexData()
  },
  // 获取首页数据
  async getIndexData() {
    const res = await reqIndexData()
    console.log(res)
    // setStorage('token', data.token);
  },
})