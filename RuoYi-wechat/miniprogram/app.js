// 执行 extendApi.js 文件，将方法挂载到 wx 全局对象身上
import './utils/extendApi'
import {
  reqLogin
} from '@/api/app';
import {
  toast
} from '@/utils/extendApi'
import {
  setStorage
} from '@/utils/storage';

App({
  // globalData 是指全局共享的数据
  globalData: {},
  onLaunch: function () {
    initLogin()
    checkUpdate()
  },

  onShow() {}
})

async function wxLogin() {
  try {
    const res = await new Promise((resolve, reject) => {
      wx.login({
        success: resolve,
        fail: reject
      });
    });

    if (res.code) {
      console.log('登录code:', res.code);
      return res.code;
    } else {
      throw new Error('登录失败！未获取到code');
    }
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
}

// 使用示例
async function initLogin() {
  const code = await wxLogin();
  if (code) {
    const res = await reqLogin(code)
    if (res.code == 0) {
      setStorage("token", res.data.token)
    } else {
      console.log("登录失败");
    }
  } else {
    console.log("登录失败");
  }
}

function checkUpdate() {
  const updateManager = wx.getUpdateManager()
  updateManager.onUpdateReady(function () {
    wx.showModal({
      title: '更新提示',
      content: '新版本已经准别好，是否重启应用？',
      complete: (res) => {
        if (res.confirm) {
          updateManager.applyUpdate()
        }
      }
    })
  })
}