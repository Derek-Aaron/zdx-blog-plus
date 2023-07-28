<template>
  <router-view />
</template>

<script setup>
import { handleThemeStyle } from '@/utils/theme'
import {nextTick, onMounted} from "vue";
import {useStore} from "./stores";
import {useDark} from "@vueuse/core";

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useStore().useSetting.theme)
	  const isDark = useDark()
	  if (isDark.value) {
		  useStore().useSetting.changeSetting({key: 'sideTheme', value: 'theme-dark'})
	  } else {
		  useStore().useSetting.changeSetting({key: 'sideTheme', value: 'theme-light'})
	  }
	  useStore().useSetting.changeSetting({key:'dark', value: isDark.value})
  })


})
</script>
