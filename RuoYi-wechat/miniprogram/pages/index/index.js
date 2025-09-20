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
  data: {
    categories: [],
    currentIndex: 0,
    // 文章分类对象
    articles: {
      "java": [{
          "id": 1,
          "tag": "简单",
          "tagType": "primary",
          "title": "Java 面向对象特性详解",
          "likes": 45,
          "views": 1203,
          "collections": 98
        },
        {
          "id": 2,
          "tag": "中等",
          "tagType": "warning",
          "title": "Java 集合框架完全指南",
          "likes": 32,
          "views": 876,
          "collections": 76
        },
        {
          "id": 3,
          "tag": "困难",
          "tagType": "danger",
          "title": "Java 并发编程深入理解",
          "likes": 58,
          "views": 942,
          "collections": 103
        },
        {
          "id": 4,
          "tag": "入门",
          "tagType": "primary",
          "title": "Java基础语法快速入门",
          "likes": 30,
          "views": 650,
          "collections": 50
        },
        {
          "id": 5,
          "tag": "进阶",
          "tagType": "warning",
          "title": "Java高级特性应用实践",
          "likes": 40,
          "views": 1000,
          "collections": 80
        },
        {
          "id": 6,
          "tag": "框架应用",
          "tagType": "success",
          "title": "Spring框架在Java中的使用",
          "likes": 48,
          "views": 1100,
          "collections": 95
        },
        {
          "id": 7,
          "tag": "性能优化",
          "tagType": "warning",
          "title": "Java程序性能调优技巧",
          "likes": 38,
          "views": 980,
          "collections": 88
        },
        {
          "id": 1,
          "tag": "简单",
          "tagType": "primary",
          "title": "Java 面向对象特性详解",
          "likes": 45,
          "views": 1203,
          "collections": 98
        },
        {
          "id": 2,
          "tag": "中等",
          "tagType": "warning",
          "title": "Java 集合框架完全指南",
          "likes": 32,
          "views": 876,
          "collections": 76
        },
        {
          "id": 3,
          "tag": "困难",
          "tagType": "danger",
          "title": "Java 并发编程深入理解",
          "likes": 58,
          "views": 942,
          "collections": 103
        },
        {
          "id": 4,
          "tag": "入门",
          "tagType": "primary",
          "title": "Java基础语法快速入门",
          "likes": 30,
          "views": 650,
          "collections": 50
        },
        {
          "id": 5,
          "tag": "进阶",
          "tagType": "warning",
          "title": "Java高级特性应用实践",
          "likes": 40,
          "views": 1000,
          "collections": 80
        },
        {
          "id": 6,
          "tag": "框架应用",
          "tagType": "success",
          "title": "Spring框架在Java中的使用",
          "likes": 48,
          "views": 1100,
          "collections": 95
        },
        {
          "id": 7,
          "tag": "性能优化",
          "tagType": "warning",
          "title": "Java程序性能调优技巧",
          "likes": 38,
          "views": 980,
          "collections": 88
        }
      ],
      "mysql": [{
          "id": 1,
          "tag": "一般",
          "tagType": "success",
          "title": "MySQL 索引优化技巧",
          "likes": 56,
          "views": 1542,
          "collections": 128
        },
        {
          "id": 2,
          "tag": "困难",
          "tagType": "danger",
          "title": "MySQL 事务隔离级别详解",
          "likes": 29,
          "views": 783,
          "collections": 65
        },
        {
          "id": 3,
          "tag": "基础",
          "tagType": "primary",
          "title": "MySQL基础操作指南",
          "likes": 35,
          "views": 900,
          "collections": 70
        },
        {
          "id": 4,
          "tag": "优化",
          "tagType": "warning",
          "title": "MySQL性能优化策略",
          "likes": 42,
          "views": 1100,
          "collections": 90
        },
        {
          "id": 5,
          "tag": "存储引擎",
          "tagType": "success",
          "title": "MySQL不同存储引擎特点对比",
          "likes": 33,
          "views": 850,
          "collections": 75
        },
        {
          "id": 6,
          "tag": "备份恢复",
          "tagType": "warning",
          "title": "MySQL数据备份与恢复方法",
          "likes": 30,
          "views": 800,
          "collections": 70
        }
      ],
      "redis": [{
          "id": 1,
          "tag": "基础",
          "tagType": "primary",
          "title": "Redis入门基础介绍",
          "likes": 40,
          "views": 1000,
          "collections": 85
        },
        {
          "id": 2,
          "tag": "数据结构",
          "tagType": "success",
          "title": "Redis常用数据结构详解",
          "likes": 50,
          "views": 1200,
          "collections": 100
        },
        {
          "id": 3,
          "tag": "缓存应用",
          "tagType": "warning",
          "title": "Redis在缓存场景中的应用",
          "likes": 45,
          "views": 1100,
          "collections": 95
        },
        {
          "id": 4,
          "tag": "持久化",
          "tagType": "danger",
          "title": "Redis持久化机制剖析",
          "likes": 38,
          "views": 950,
          "collections": 88
        },
        {
          "id": 5,
          "tag": "集群部署",
          "tagType": "warning",
          "title": "Redis集群部署与管理",
          "likes": 35,
          "views": 900,
          "collections": 80
        },
        {
          "id": 6,
          "tag": "性能优化",
          "tagType": "warning",
          "title": "Redis性能优化策略",
          "likes": 32,
          "views": 880,
          "collections": 78
        }
      ],
      "python": [{
          "id": 1,
          "tag": "简单",
          "tagType": "primary",
          "title": "Python基础语法学习",
          "likes": 50,
          "views": 1300,
          "collections": 110
        },
        {
          "id": 2,
          "tag": "进阶",
          "tagType": "warning",
          "title": "Python高级编程技巧",
          "likes": 42,
          "views": 1050,
          "collections": 95
        },
        {
          "id": 3,
          "tag": "数据处理",
          "tagType": "danger",
          "title": "Python数据处理与分析应用",
          "likes": 60,
          "views": 1400,
          "collections": 120
        },
        {
          "id": 4,
          "tag": "Web开发",
          "tagType": "warning",
          "title": "Python在Web开发中的应用",
          "likes": 48,
          "views": 1150,
          "collections": 105
        },
        {
          "id": 5,
          "tag": "自动化脚本",
          "tagType": "success",
          "title": "Python编写自动化脚本实例",
          "likes": 38,
          "views": 980,
          "collections": 88
        },
        {
          "id": 6,
          "tag": "机器学习",
          "tagType": "warning",
          "title": "Python在机器学习领域的应用",
          "likes": 55,
          "views": 1350,
          "collections": 115
        }
      ],
      "vue": [{
          "id": 1,
          "tag": "基础",
          "tagType": "primary",
          "title": "Vue基础入门教程",
          "likes": 45,
          "views": 1100,
          "collections": 95
        },
        {
          "id": 2,
          "tag": "组件化",
          "tagType": "success",
          "title": "Vue组件化开发详解",
          "likes": 52,
          "views": 1250,
          "collections": 108
        },
        {
          "id": 3,
          "tag": "路由应用",
          "tagType": "warning",
          "title": "Vue路由的使用与配置",
          "likes": 48,
          "views": 1180,
          "collections": 102
        },
        {
          "id": 4,
          "tag": "状态管理",
          "tagType": "danger",
          "title": "Vuex状态管理实践",
          "likes": 42,
          "views": 1050,
          "collections": 92
        },
        {
          "id": 5,
          "tag": "项目实战",
          "tagType": "warning",
          "title": "Vue项目实战案例解析",
          "likes": 50,
          "views": 1200,
          "collections": 105
        },
        {
          "id": 6,
          "tag": "性能优化",
          "tagType": "warning",
          "title": "Vue应用性能优化策略",
          "likes": 38,
          "views": 980,
          "collections": 88
        }
      ],
      "android": [{
          "id": 1,
          "tag": "入门",
          "tagType": "primary",
          "title": "Android开发入门基础",
          "likes": 40,
          "views": 1000,
          "collections": 85
        },
        {
          "id": 2,
          "tag": "UI设计",
          "tagType": "success",
          "title": "Android UI界面设计实践",
          "likes": 48,
          "views": 1150,
          "collections": 100
        },
        {
          "id": 3,
          "tag": "功能开发",
          "tagType": "warning",
          "title": "Android常见功能模块开发",
          "likes": 50,
          "views": 1200,
          "collections": 105
        },
        {
          "id": 4,
          "tag": "性能优化",
          "tagType": "danger",
          "title": "Android应用性能优化技巧",
          "likes": 38,
          "views": 980,
          "collections": 88
        },
        {
          "id": 5,
          "tag": "框架应用",
          "tagType": "warning",
          "title": "Android开发框架应用实例",
          "likes": 42,
          "views": 1050,
          "collections": 92
        },
        {
          "id": 6,
          "tag": "发布上线",
          "tagType": "success",
          "title": "Android应用发布上线流程",
          "likes": 35,
          "views": 900,
          "collections": 80
        }
      ],
      "wechat": [{
          "id": 1,
          "tag": "基础功能",
          "tagType": "primary",
          "title": "微信基础功能使用介绍",
          "likes": 60,
          "views": 1500,
          "collections": 130
        },
        {
          "id": 2,
          "tag": "小程序开发",
          "tagType": "success",
          "title": "微信小程序开发入门",
          "likes": 50,
          "views": 1200,
          "collections": 105
        },
        {
          "id": 3,
          "tag": "公众号运营",
          "tagType": "warning",
          "title": "微信公众号运营策略",
          "likes": 48,
          "views": 1180,
          "collections": 102
        },
        {
          "id": 4,
          "tag": "支付功能",
          "tagType": "danger",
          "title": "微信支付功能详解与应用",
          "likes": 45,
          "views": 1100,
          "collections": 98
        },
        {
          "id": 5,
          "tag": "企业微信应用",
          "tagType": "warning",
          "title": "企业微信的使用与管理",
          "likes": 42,
          "views": 1050,
          "collections": 92
        },
        {
          "id": 6,
          "tag": "营销推广",
          "tagType": "warning",
          "title": "微信平台营销推广技巧",
          "likes": 38,
          "views": 980,
          "collections": 88
        }
      ]
    },
    currentCategory: ''
  },
  // 监听页面的加载
  onLoad() {
    // 页面加载后检查是否有token
    this.checkTokenAndLoadData();

    const categories = Object.keys(this.data.articles);
    // 如果有分类，将第一个分类设为默认选中
    if (categories.length > 0) {
      this.setData({
        currentCategory: categories[0],
        categories
      });
    }

    // 调试用：检查是否获取到分类
    console.log('获取到的分类:', categories);
  },

  // 处理选项点击事件
  handleItemClick(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      currentIndex: index
    });

    // 这里可以添加点击后的其他逻辑
    console.log('选中了:', this.data.categories[index]);
  },

  switchCategory(e) {
    console.log(e);
    const category = e.detail.title;
    console.log("更改分类为 ==> ", category);
    this.setData({
      currentCategory: category
    });
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