<template>
  <template v-if="!item.children && item.type == 'MENU'">
    <el-menu-item :index="item.action" :class="{ 'submenu-title-noDropdown': !isNest }">
      <svg-icon :icon-class="item.icon" />
      <template #title><span class="menu-title">{{ item.name }}</span></template>
    </el-menu-item>
  </template>

  <el-sub-menu v-else ref="subMenu" :index="item.id" popper-append-to-body>
    <template #title>
      <svg-icon :icon-class="item.icon" />
      <span class="menu-title">{{ item.name }}</span>
    </template>

    <sidebar-item v-for="child in item.children" :key="child.action" :is-nest="true" :item="child" class="nest-menu" />
  </el-sub-menu>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon/index.vue';
defineProps({
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    required: false
  },
});
</script>

<style scoped></style>