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
    const roles = ref([])
    const permissions = ref([])


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
              roles.value = res.data.roles
              permissions.value = res.data.permissions
              resolve()
          }).catch(err => {
              reject(err)
          })
      } )
    }

    const doLogout = () => {
        logout().then((res ) => {
            removeToken()
            router.push("/").then(r => {})
        })
    }

    const checkPermission = (val) => {
       return new Promise((resolve) => {
           if (permissions.value.indexOf("*::||::*") !== -1) {
               return resolve(true);
           }
           if (!val) {
              return resolve(false)
           }
           return resolve(permissions.value.indexOf(val) !== -1);
       })

    }

    return {token, username, nickname, avatar,roles, permissions ,doLogin, doInfo, doLogout, checkPermission}
})