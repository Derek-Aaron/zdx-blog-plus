import { useEventListener } from "@vueuse/core";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from './router'
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";

NProgress.configure({ showSpinner: false });


function titleChange() {
	// 动态标题
	let OriginTitile = document.title;
	let titleTime
	useEventListener(document, "visibilitychange", () => {
		if (document.hidden) {
			//离开当前页面时标签显示内容
			document.title = "w(ﾟДﾟ)w 不要走！再看看嘛！";
			clearTimeout(titleTime);
		} else {
			//返回当前页面时标签显示内容
			document.title = "♪(^∇^*)欢迎回来！";
			location.replace(location.href)
			//两秒后变回正常标题
			titleTime = setTimeout(() => {
				document.title = OriginTitile;
			}, 2000);
		}
	});
}
titleChange();

router.beforeEach((to, from, next) => {
    useStore().useSetting.setTitle(to.meta.title)
    NProgress.start()
    if (to.path === "/" || to.path === '/callback'){
        next()
    } else {
        if (useStore().usePermission.routers.length === 0) {
            useStore().usePermission.refreshRouters().then(r => {})
        }
	    const permission = to.meta.permission
        if (useStore().useUser.avatar === '' || useStore().useUser.nickname === '' || useStore().useUser.username === '') {
            useStore().useUser.doInfo().then(r => {
				if (!permission) {
					next()
				} else {
					useStore().useUser.checkPermission(to.meta.permission).then((value) => {
						if (value) {
							next()
						} else {
							ElMessage.error("权限异常，请联系管理员")
							location.href = "/"
						}
					})
				}
            })
        }
        next()
    }

})
router.afterEach(() => {
    NProgress.done()
})