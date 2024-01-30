<template>
  <div class="page-header">
    <h1 class="page-title">关于</h1>
    <img class="page-cover" src="https://static.zhaodengxuan.top/config/2000171e559.jpg"
      alt="" />
    <Waves></Waves>
  </div>
  <div class="bg">
    <div class="page-container">
      <div class="avatar-box">
        <img class="author-avatar" :src="blog.blogInfo.siteConfig.authorAvatar"  alt=""/>
      </div>
      <v-md-preview class="md" :text="blog.blogInfo.siteConfig.aboutMe"></v-md-preview>
    </div>
  </div>
</template>

<script setup>
import useStore from "@/stores";
import Waves from "@/components/Waves/index.vue"
const { blog } = useStore();
</script>

<style scoped>
.author-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  transition: all 0.5s;
}

.author-avatar:hover {
  transform: rotate(360deg);
}
</style>
