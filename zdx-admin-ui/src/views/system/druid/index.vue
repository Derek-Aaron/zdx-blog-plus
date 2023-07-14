<script setup>
import iFrame from '@/components/iFrame/index.vue'
import { ref } from 'vue';

const url = ref('http://localhost:8080/druid/login.html');
</script>

<template>
    <div>
      <i-frame v-model:src="url"></i-frame>
   </div>
</template>

<style lang="scss" scoped>

</style>