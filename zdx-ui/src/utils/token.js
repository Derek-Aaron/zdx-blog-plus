import Cookies from "js-cookie";

const TokenKey = "Token";

// 我网站的链接是www.zhaodengxuan.top，去前面的www
const domain = ".zhaodengxuan.top";

// token前缀
export let token_prefix = "Bearer ";

export function getToken() {
  return Cookies.get(TokenKey);
}

// 本地运行记得删除domain
export function setToken(token) {
  return Cookies.set(TokenKey, token, { domain: domain });
}

export function removeToken() {
  return Cookies.remove(TokenKey, { domain: domain });
}
