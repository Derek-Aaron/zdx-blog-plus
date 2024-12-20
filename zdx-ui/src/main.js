import "@/assets/fonts/font.css";
import "@/assets/styles/index.scss";
import "@/assets/katex/katex.min.css"
import "@/assets/katex/katex.min"
import SvgIcon from "@/components/SvgIcon/index.vue";
import * as directive from "@/directive";
import "@/permission";
import { titleChange } from "@/utils/title";
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import naive from "naive-ui"
import App from './App.vue'
import router from './router'
import error from "./assets/images/404.gif";
import loading from "./assets/images/loading.gif";
import { VueMasonryPlugin } from "vue-masonry";
import Prism from "prismjs";
import createKatexPlugin from "@kangc/v-md-editor/lib/plugins/katex/cdn";
import createTodoListPlugin from "@kangc/v-md-editor/lib/plugins/todo-list/index";
import "@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css";
import VMdPreview from "@kangc/v-md-editor/lib/preview";
import "@kangc/v-md-editor/lib/theme/style/vuepress.css";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress.js";
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
import '@/utils/fireworks'

import "swiper/css";
import "swiper/css/autoplay";
import "swiper/css/mousewheel";
import "swiper/css/navigation";
import "swiper/css/pagination";
import VueViewer from "v-viewer";
import "viewerjs/dist/viewer.css";
import "virtual:svg-icons-register";
import lazyPlugin from "vue3-lazy";

const app = createApp(App)
Object.keys(directive).forEach((key) => {
	app.directive(key, directive[key]);
});

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate)

VMdPreview.use(vuepressTheme, {
	Prism,
})
	.use(createTodoListPlugin())
	.use(createKatexPlugin())
	.use(createCopyCodePlugin());
app.use(VMdPreview)
app.use(pinia)
app.use(router)
app.use(naive)
app.use(VueViewer);
app.use(lazyPlugin, {
	loading,
	error,
});
app.use(VueMasonryPlugin);


app.component("svg-icon", SvgIcon);
app.mount('#app')
titleChange();