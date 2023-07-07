<template>
  <router-view />
</template>

<script setup>
import { handleThemeStyle } from '@/utils/theme'
import {nextTick, onMounted} from "vue";
import {useStore} from "./stores";

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useStore().useSetting.theme)
  })
})
</script>
