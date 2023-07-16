<template>
  <n-pagination class="pagination" v-model:page="currentPage" :page-count="total" :page-slot="5">
    <template #prev>
      <svg-icon icon-class="angle-left"></svg-icon>
    </template>
    <template #next>
      <svg-icon icon-class="angle-right"></svg-icon>
    </template>
  </n-pagination>
</template>

<script setup>
import {computed} from "vue";

const emit = defineEmits(["update:current"]);
const props = defineProps({
  current: {
    type: Number,
    default: 1,
  },
  total: {
    type: Number,
    default: 0,
  }
});
const currentPage = computed({
  get: () => props.current,
  set: value => {
    emit("update:current", value);
  }
});
</script>

<style scoped></style>