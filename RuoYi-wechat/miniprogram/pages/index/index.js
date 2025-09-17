import {
  reqIndexData
} from '@/api/index';
// 导入本地存储 api
import {
  setStorage,
  getStorage
} from '@/utils/storage';
// 导入封装通用模块方法
import {
  toast
} from '@/utils/extendApi'
Page({
  data: {},
  // 监听页面的加载
  onLoad() {
    // 页面加载后检查是否有token
    this.checkTokenAndLoadData();
  },

  /**
   * 检查是否有token，没有则循环等待，有则加载数据
   */
  checkTokenAndLoadData() {
    const token = getStorage("token");
    if (token) {
      // 有token，直接请求数据
      this.getIndexData();
    } else {
      // 使用setTimeout递归检查，每隔50ms检查一次
      setTimeout(() => {
        this.checkTokenAndLoadData();
      }, 50);
    }
  },
  // 获取首页数据
  async getIndexData() {
    const res = await reqIndexData()
    console.log(res)
  },
})