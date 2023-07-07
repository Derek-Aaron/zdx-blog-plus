<template>
<!--  <template v-if="-->
<!--            hasOneShowingChild(item.children, item) &&-->
<!--            (!onlyOneChild.children || onlyOneChild.noShowingChildren) &&-->
<!--            (!item.alwaysShow)-->
<!--        ">-->
<!--    <el-menu-item :index="resolvePath(onlyOneChild.action)" :class="{ 'submenu-title-noDropdown': !isNest }">-->
<!--      <el-icon>-->
<!--        <svg-icon v-if="onlyOneChild.icon"-->
<!--                  :icon-class="onlyOneChild.icon" />-->
<!--      </el-icon>-->
<!--      <template #title>-->
<!--        {{ onlyOneChild.name }}-->
<!--      </template>-->
<!--    </el-menu-item>-->
<!--  </template>-->
<!--  <el-sub-menu v-else :index="item.id">-->
<!--    <template #title>-->
<!--      <el-icon>-->
<!--        <svg-icon v-if="item.icon" :icon-class="item.icon" />-->
<!--      </el-icon>-->
<!--      <span class="menu-title">{{ item.name }}</span>-->
<!--    </template>-->
<!--    <sidebar-item v-for="child in item.children" :key="child.action" :item="child" :is-nest="true"-->
<!--                  :base-path="''" class="nest-menu" />-->
<!--  </el-sub-menu>-->
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
        <el-menu-item :index="resolvePath(onlyOneChild.action)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon :icon-class="onlyOneChild.icon || item.icon"/>
          <template #title><span class="menu-title">{{ onlyOneChild.name }}</span></template>
        </el-menu-item>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="item.id" popper-append-to-body>
      <template #title>
        <svg-icon :icon-class="item.icon" />
        <span class="menu-title">{{ item.name }}</span>
      </template>

      <sidebar-item
          v-for="child in item.children"
          :key="child.action"
          :is-nest="true"
          :item="child"
          class="nest-menu"
      />
    </el-sub-menu>
</template>

<script setup>
import SvgIcon from '@/components/SvgIcon/index.vue';
import { ref } from "vue";
const onlyOneChild = ref();
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
const hasOneShowingChild = (children, parent) => {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter((item) => {
    if (item.hidden) {
      return false;
    } else {
      onlyOneChild.value = item;
      return true;
    }
  });
  if (showingChildren.length === 1) {
    return true;
  }
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true };
    return true;
  }
  return false;
}
const resolvePath = (routePath) => {
  return getNormalPath('/' + routePath)
}
const getNormalPath = (p) => {
  if (p.length === 0 || !p || p === 'undefined') {
    return p
  };
  let res = p.replace('//', '/')
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1)
  }
  return res;
}
</script>

<style scoped>

</style>