<template>
  <transition-group name="fade-transform" mode="out-in">
    <inner-link
      v-for="(item, index) in tagsViewStore.iframeViews"
      :key="item.path"
      :iframeId="'iframe' + index"
      v-show="route.path === item.path"
      :src="item.meta.link"
    ></inner-link>
  </transition-group>
</template>

<script setup>
import InnerLink from "../InnerLink/index.vue"
import {useStore} from "@/stores";
import {useRoute} from "vue-router";

const route = useRoute();
const tagsViewStore = useStore().useTagsView
</script>
