import {defineStore} from 'pinia'
import {reactive, ref} from "vue";

export const useAppStore = defineStore('useAppStore', () => {
    const sidebar = reactive({
        opened: true,
        withoutAnimation: false,
        hide: false
    })

    const device = ref('desktop')
    const size = ref("default")

    const toggleSideBar = (val) => {
        if (sidebar.hide) {
            return false
        }
        sidebar.opened = !sidebar.opened
        sidebar.withoutAnimation = val
    }

    const closeSideBar = ({val}) => {
        sidebar.opened = false
        sidebar.withoutAnimation = val
    }

    const toggleDevice = (val) => {
        device.value = val
    }

    const setSize = (val) => {
        size.value = val
    }

    const toggleSideBarHide = (val) => {
        sidebar.hide = val
    }
    return {sidebar, device, size, toggleSideBar, closeSideBar, toggleDevice, setSize, toggleSideBarHide}
}, {
    persist: {
        key:'app',
        storage: sessionStorage
    }
})