import {defineStore} from 'pinia'
import {ref} from "vue";
import {getRouters} from "@/api/login";


export const usePermissionStore = defineStore('usePermissionStore', () => {
    const routers = ref([])

    const refreshRouters = () => {
       return new Promise((resolve, reject) => {
           getRouters().then(res => {
               routers.value = res.data
               resolve()
           }).catch(err => {
               reject(err)
           })
       })
    }


    return {routers, refreshRouters}
})