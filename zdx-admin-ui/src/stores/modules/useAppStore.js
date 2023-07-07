import {defineStore} from 'pinia'
import {reactive, ref} from "vue";
import Cookies from 'js-cookie'

export const useAppStore = defineStore('useAppStore', () => {
    const sidebar = reactive({
        opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
        withoutAnimation: false,
        hide: false
    })

    const device = ref('desktop')
    const size = ref(Cookies.get('size') || 'default')

    const toggleSideBar = (val) => {
        if (sidebar.hide) {
            return false
        }
        sidebar.opened = !sidebar.opened
        sidebar.withoutAnimation = val
        if (sidebar.opened) {
            Cookies.set('sidebarStatus', 1)
        } else {
            Cookies.set('sidebarStatus', 0)
        }
    }

    const closeSideBar = ({val}) => {
        Cookies.set('sidebarStatus', 0)
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
})