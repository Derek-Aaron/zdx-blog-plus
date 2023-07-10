<script setup>
import { onMounted, ref } from "vue"
import { listUserAll } from "@/api/us/user"

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

const filterMethod = (val) => {
    // if (val && val !== '') {
    //     listUserAll({params: val}).then(res => {
    //         let array = []
    //         for (const item of res.data) {
    //             let obj = {}
    //             obj.id = 'us:user:' + item.id
    //             obj.name = item.username
    //             array.push(obj)
    //         }
    //         data.value = array
    //     })
    // }
    return data.value
}

onMounted(() => {
    listUserAll().then(res => {
        let array = []
        for (const item of res.data) {
            let obj = {}
            obj.id = 'us:user:' + item.id
            obj.name = item.username
            array.push(obj)
        }
        data.value = array
    })
})
</script>

<template>
    <el-transfer :titles="['全部', '已有']" filterable :filter-method="filterMethod" filter-placeholder="请输入"
        :props="{ key: 'id', label: 'name' }" v-model="$props.modelValue" :data="data" @change="change" />
</template>

<style lang="scss" scoped></style>