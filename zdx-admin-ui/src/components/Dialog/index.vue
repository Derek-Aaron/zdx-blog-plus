<template>
    <el-dialog :model-value="$props.dialog" :title="$props.title" :width="$props.width" :before-close="handleClose"
        :center="$props.center" draggable :fullscreen="$props.fullscreen">
        <slot name="content"></slot>
        <template #footer>
            <span class="dialog-footer">
                <slot name="footer" />
                <el-button @click="$emit('close')">关闭</el-button>
               
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
const props = defineProps({
    title: {
        type: String,
        default: '',
        required: true
    },
    dialog: {
        type: Boolean,
        default: false,
        required: true
    },
    width: {
        type: String,
        default: '30%'
    },
    center: {
        type: Boolean,
        default: false
    },
    fullscreen: {
        type: Boolean,
        default: false
    },
})

const emits = defineEmits(['close'])

const handleClose = (done) => {
    done()
    emits('close')
}
</script>

<style lang="scss" scoped></style>