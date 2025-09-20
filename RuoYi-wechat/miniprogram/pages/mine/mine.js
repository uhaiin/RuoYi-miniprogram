import {
  ComponentWithStore
} from 'mobx-miniprogram-bindings'
import {
  userStore
} from '@/stores/userstore';
import {
  toast
} from '@/utils/extendApi'

ComponentWithStore({
  // 页面的初始数据
  data: {
    // 初始化第二个面板数据
    initpanel: [{
        url: '/pages/code/code',
        title: '取码记录',
        iconfont: 'icon-dingdan'
      },
      {
        url: '/pages/password/password',
        title: '修改密码',
        iconfont: 'icon-dingdan'
      },
      {
        url: '/pages/order_no/order_no',
        title: '单号查询',
        iconfont: 'icon-lipinka'
      },
      {
        url: '/pages/code_no/code_no',
        title: '卡密查询',
        iconfont: 'icon-tuikuan'
      }
    ],
    initAdminPanel: [{
        url: '/pages/batch_import/batch_import',
        title: '批量导入',
        iconfont: 'icon-lipinka'
      },
      {
        url: '/pages/inventory/inventory',
        title: '库存统计',
        iconfont: 'icon-tuikuan'
      }
    ],
    initSuPanel: [{
      url: '/pages/employee/employee',
      title: '客服管理',
      iconfont: 'icon-tuikuan'
    }]
  },

  storeBindings: {
    store: userStore,
    fields: ['token', 'userInfo', 'admin']
  },

  methods: {
    // 跳转到登录页面
    toLoginPage() {
      wx.navigateTo({
        url: '/pages/login/login'
      })
    },
    exit() {
      // 清除所有缓存
      wx.clearStorageSync()

      // 清除初始值
      this.setData({
        token: "",
        admin: "",
        userInfo: {}
      })

      toast({
        title: "成功退出"
      })
    }
  },

})