<template>
    <el-dialog :model-value="$props.dialog" :title="$props.title" :width="$props.width" :before-close="handleClose"
        :center="$props.center" top="100%" draggable :fullscreen="$props.fullscreen" close-on-press-escape>
        <slot name="content"></slot>
        <template #footer>
            <span class="dialog-footer">
                <slot name="footer" />
                <el-button v-if="$props.isOpenClose" @click="$emit('close')">关闭</el-button>
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
        default: '50%'
    },
    center: {
        type: Boolean,
        default: false
    },
    fullscreen: {
        type: Boolean,
        default: false
    },
	isOpenClose:{
		type: Boolean,
		default: true
	}
})

const emits = defineEmits(['close'])

const handleClose = (done) => {
    done()
    emits('close')
}
</script>

<style lang="scss" scoped></style>