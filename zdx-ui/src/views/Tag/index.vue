<template>
  <div class="page-header">
    <h1 class="page-title">标签</h1>
    <img class="page-cover" src="https://static.zhaodengxuan.top/config/172309cWIuR.jpg"
      alt="">
    <!-- 波浪 -->
    <Waves></Waves>
  </div>
  <div class="bg">
    <div class="page-container">
      <div class="tag-cloud">
        <router-link :to="`/tag/${tag.id}`" class="tag-item" v-for="tag in tagList" :key="tag.id"
          :style="{ 'font-size': getSize(tag.articleCount), 'color': getRandomColor() }">
          {{ tag.name }}
          <sup>{{ tag.articleCount }}</sup>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup >
import { getTagList } from "@/api/tag";
import {onMounted, ref} from "vue";
const getSize = (freq) => {
  return ((1 + 6 * freq / 10) / 3) * 2 + "rem";
};
const getRandomColor = () => {
  return "rgb(" + Math.round(Math.random() * 255) + "," + Math.round(Math.random() * 255) + "," + Math.round(Math.random() * 255) + ")";
};
const tagList = ref([]);
onMounted(() => {
  getTagList().then((res) => {
    tagList.value = res.data;
  });
});
</script>

<style lang="scss" scoped>
.tag-cloud {
  text-align: center;
}

.tag-item {
  display: inline-block;
  padding: 0 0.5rem;
  transition: all 0.3s;

  &:hover {
    transform: scale(1.1);
  }
}
</style>