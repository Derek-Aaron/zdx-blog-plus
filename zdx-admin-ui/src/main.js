import Cookies from 'js-cookie'

import '@/assets/styles/index.scss' // global css

import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import hljs from 'highlight.js';

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'
import elementIcons from '@/components/SvgIcon/svgicon'
import ElementPlus from 'element-plus'
import locale from 'element-plus/es/locale/lang/zh-cn' // 中文语言
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import plugins from '@/plugins'
import directive from "@/directive"
import '@/permission'
import App from './App.vue'
import router from './router'

const app = createApp(App)

VMdEditor.use(githubTheme, {
    Hljs: hljs,
});

let pinia = createPinia();
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)
app.use(VMdEditor);
app.use(plugins)
app.use(directive)
app.use(ElementPlus,{
    locale: locale,
    size: Cookies.get('size') || 'default'
})
app.use(elementIcons)

app.config.warnHandler = (msg) => {
    console.warn(msg)
}

app.config.errorHandler = (msg) => {
    console.error(msg)
}

app.component('svg-icon', SvgIcon)

app.mount('#app')
