import {defineStore} from 'pinia'
import {ref} from "vue";
import {getToken, removeToken, setToken} from "@/utils/auth";
import defAva from '@/assets/images/profile.jpg'
import {info, login, logout} from "@/api/login";
import router from "@/router";


export const useUserStore = defineStore('useUserStore', () => {
    const token = ref(getToken())
    const username = ref('')
    const nickname = ref('')
    const avatar = ref('')


    const doLogin = (data) => {
        return new Promise((resolve, reject) => {
            login(data).then(res => {
                token.value = res.data.token
                setToken(token.value)
                resolve(res)
            }).catch(err => {
                reject(err)
            })
        })
    }

    const doInfo = () => {
      return new Promise((resolve, reject) => {
          info().then((res) => {
              username.value = res.data.usernname
              nickname.value = res.data.nickname;
              avatar.value = (res.data.avatar === "" || res.data.avatar == null) ? defAva : res.data.avatar;
          })
      } )
    }

    const doLogout = () => {
        logout().then((res ) => {
            removeToken()
            router.push("/").then(r => {})
        })
    }

    return {token, username, nickname, avatar, doLogin, doInfo, doLogout}
})