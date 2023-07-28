import defaultSettings from '@/settings'
import {useDynamicTitle} from '@/utils/dynamicTitle'
import {defineStore} from 'pinia'
import {ref} from "vue";

const storageSetting = JSON.parse(localStorage.getItem('layout-setting')) || ''

export const useSettingsStore = defineStore('useSettingsStore', () => {
    const title = ref('')
    const theme = ref(storageSetting.theme || '#409EFF')
    const sideTheme = ref(storageSetting.sideTheme || defaultSettings.sideTheme)
    const showSettings = ref(defaultSettings.showSettings)
    const topNav = ref(storageSetting.topNav === undefined ? defaultSettings.topNav : storageSetting.topNav)
    const tagsView = ref(storageSetting.tagsView === undefined ? defaultSettings.tagsView : storageSetting.tagsView)
    const fixedHeader = ref(storageSetting.fixedHeader === undefined ? defaultSettings.fixedHeader : storageSetting.fixedHeader)
    const sidebarLogo = ref(storageSetting.sidebarLogo === undefined ? defaultSettings.sidebarLogo : storageSetting.sidebarLogo)
    const dynamicTitle = ref(storageSetting.dynamicTitle === undefined ? defaultSettings.dynamicTitle : storageSetting.dynamicTitle)
    const useDark = ref(defaultSettings.useDark)
    const changeSetting = (data) => {
        const { key, value } = data
        if (key === 'theme') {
            theme.value = value
        } else if (key === 'sideTheme') {
            sideTheme.value = value
        } else if (key === 'showSettings') {
            showSettings.value = value
        } else if (key === 'topNav') {
            topNav.value = value
        } else if (key === 'tagsView') {
            tagsView.value = value
        } else if (key === 'fixedHeader') {
            fixedHeader.value = value
        } else if (key === 'sidebarLogo') {
            sidebarLogo.value = value
        } else if (key === 'dynamicTitle') {
            dynamicTitle.value = value
        } else if (key === 'dark') {
            useDark.value = value
        }
    }

    const setTitle = (val) => {
        title.value = val
        useDynamicTitle();
    }

    return {title, theme, sideTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle,useDark, changeSetting, setTitle}
}, {
    persist:{
        key:'setting',
        storage: sessionStorage
    }
})
