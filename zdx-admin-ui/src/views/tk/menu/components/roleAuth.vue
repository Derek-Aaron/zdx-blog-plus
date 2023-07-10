<script setup>
import { onMounted, ref } from "vue"
import { list } from "@/api/base"
defineProps({
    modelValue: {
        type: Array,
        default: []
    }
})

const data = ref([])
const emits = defineEmits(['change', 'update:modelValue'])

const change = (val, direction, move) => {
    let obj = {}
    if (direction === 'left') {
        obj.type = 'del'
        obj.subjects = move
    }
    if (direction === 'right') {
        obj.type = 'add'
        obj.subjects = val
    }
    emits('change', obj)
    emits('update:modelValue', val)
}
onMounted(() => {
    list("role").then(res => {
        let array = []
        for (const item of res.data) {
            let obj = {}
            obj.id = 'us:role:' + item.id
            obj.name = item.name
            array.push(obj)
        }
        data.value = array
    })
})
</script>

<template>
    <el-transfer :titles="['全部', '已有']" :props="{ key: 'id', label: 'name' }" v-model="$props.modelValue" :data="data" @change="change" />
</template>

<style lang="scss" scoped></style>  