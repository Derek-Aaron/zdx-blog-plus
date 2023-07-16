<template>
  <meting-js v-if="blog.blogInfo.siteConfig && blog.blogInfo.siteConfig.isMusic" server="netease" type="playlist"
    :id="blog.blogInfo.siteConfig.musicId" fixed="true" theme="#e9546b"></meting-js>
</template>

<script setup>
import useStore from "@/stores";
const { blog } = useStore();
</script>

<style scoped></style>
