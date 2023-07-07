import Cookies from 'js-cookie'

import '@/assets/styles/index.scss' // global css


import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'
import elementIcons from '@/components/SvgIcon/svgicon'
import ElementPlus from 'element-plus'
import locale from 'element-plus/lib/locale/lang/zh-cn' // 中文语言
import 'element-plus/dist/index.css'
import plugins from '@/plugins'
import '@/permission'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(plugins)
app.use(ElementPlus,{
    locale: locale,
    size: Cookies.get('size') || 'default'
})
app.use(elementIcons)

app.component('svg-icon', SvgIcon)

app.mount('#app')
