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
  data: {},

  storeBindings: {
    store: userStore,
    fields: ['token', 'userInfo']
  },

  methods: {},

})