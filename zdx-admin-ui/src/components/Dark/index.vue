<template>
    <el-switch v-model="isDark" class="mt-2" style="margin-left: 24px" inline-prompt :active-icon="Check"
        :inactive-icon="Close" />
</template>

<script setup>
import { useDark, useToggle } from '@vueuse/core'

const isDark = useDark()
useToggle(isDark)
</script>

<style lang="scss" scoped></style>