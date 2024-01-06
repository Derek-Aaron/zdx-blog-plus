import router from "@/router";
import useStore from "@/stores";
import {getToken, setToken} from "@/utils/token";
import NProgress from "nprogress";

NProgress.configure({
  easing: "ease",
  speed: 500,
  showSpinner: false,
  trickleSpeed: 200,
  minimum: 0.3,
});

router.beforeEach((to, from, next) => {
  const { user } = useStore();
  NProgress.start();
  if (to.query.token) {
    setToken(to.query.token)
    user.doUserInfo();
    if (user.email === "") {
      window.$message?.warning("请绑定邮箱以便及时收到回复");
    } else {
      window.$message?.success("登录成功");
    }
    // 跳转回原页面
    const loginUrl = user.path;
    if (loginUrl != null && loginUrl !== "") {
      to.path = loginUrl
    } else {
      to.path = "/"
    }
    next(to.path)
  }
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  if (getToken()) {    
    if (user.id === undefined) {
      user.doUserInfo()
        .then(() => next())
        .catch(() => {
          user.doLogout().then(() => {
            window.$message?.warning("凭证失效，请重新登录");
            next();
          });
        });
    } else {
      next();
    }
  } else {
    next();
  }
});
router.afterEach(() => {
  NProgress.done();
});
