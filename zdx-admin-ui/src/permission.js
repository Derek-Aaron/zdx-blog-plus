
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from './router'
import {getToken} from "@/utils/auth";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";

NProgress.configure({ showSpinner: false });

function loadingData(obj, next) {
    // useTagsViewStore().addTags(obj)
    // if (useUser().userSession.userId === "") {
    //     useUser().getUserInfo().then(() => {
    //         useRouter().getRouter().then((res) => {
    //             next()
    //         })
    //     }).catch((error) => {
    //         console.log(error)
    //         // user().getLogout().then(() => {
    //         //     ElMessage.error(error)
    //         //     next({path:'/'})
    //         // })
    //     })
    // } else {
    //     next()
    // }
}

router.beforeEach((to, from, next) => {
    useStore().useSetting.setTitle(to.meta.title)
    NProgress.start()
    if (to.path === "/" || to.path === '/callback'){
        next()
    } else {
        if (useStore().usePermission.routers.length === 0) {
            useStore().usePermission.refreshRouters().then(r => {})
        }
        if (useStore().useUser.avatar === '' || useStore().useUser.nickname === '' || useStore().useUser.username === '') {
            useStore().useUser.doInfo().then(r => {})
        }
        next()
    }

})
router.afterEach(() => {
    NProgress.done()
})