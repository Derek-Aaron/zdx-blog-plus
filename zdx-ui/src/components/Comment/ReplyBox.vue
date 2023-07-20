<template>
	<div class="reply-box" v-if="show">
		<div class="box-normal">
			<div class="reply-box-avatar">
				<img class="shoka-avatar" v-if="user.avatar" :src="user.avatar" alt=""/>
				<img class="shoka-avatar" v-else :src="blog.blogInfo.siteConfig.touristAvatar" alt=""/>
			</div>
			<div class="reply-box-warp">
        <textarea class="reply-box-textarea" v-model="content" :style="sendActive ? lineStyle : ''"
				  @input.prevent="inputActive" :placeholder="placeholderText"></textarea>
			</div>
			<div class="reply-box-send" :class="sendActive ? 'send-active' : ''" @click="handleAdd">
				评论
			</div>
		</div>
		<div class="box-expand">
			<Emoji @add-emoji="handleEmoji" @choose-type="handleType"></Emoji>
		</div>
	</div>
</template>

<script setup>
import Emoji from "@/components/Emoji/index.vue"
import useStore from "@/stores";
import emojiList from "@/utils/emoji";
import tvList from "@/utils/tv";
import {computed, reactive, toRefs} from "vue";
import {addComment} from "@/api/comment";

const {user, blog, app} = useStore();
const lineStyle = {
	lineHeight: "normal",
	borderColor: "#ed6ea0",
	backgroundColor: "var(--grey-0)",
};
const emit = defineEmits(["reload"]);
const props = defineProps({
	commentType: {
		type: String,
	},
	show: {
		type: Boolean,
		default: true,
	},
	typeId: {
		type: String,
	},
});
const data = reactive({
	nickname: "",
	sendActive: false,
	show: props.show,
	content: "",
	emojiType: 0,
	commentForm: {
		typeId: props.typeId,
		commentType: props.commentType,
		parentId: undefined,
		replyId: undefined,
		toUid: undefined,
		content: "",
	},
})
const {nickname, sendActive, show, content, emojiType, commentForm} = toRefs(data);
const placeholderText = computed(() =>
	nickname.value ? `回复 @${nickname.value}：` : "发一条友善的评论"
);
const inputActive = () => {
	if (content.value.length === 0) {
		sendActive.value = false;
	} else {
		sendActive.value = true;
	}
};
const handleEmoji = (key) => {
	content.value += key;
	sendActive.value = true;
};
const handleType = (key) => {
	emojiType.value = key;
};
const handleAdd = () => {
	if (!user.id) {
		app.setLoginFlag(true);
		return;
	}
	if (content.value.trim() === "") {
		window.$message?.error("评论不能为空");
		return;
	}
	// 解析表情
	commentForm.value.content = content.value.replace(/\[.+?\]/g, (str) => {
		if (emojiType.value === 0) {
			if (emojiList[str] === undefined) {
				return str;
			}
			return (
				"<img src= '" +
				emojiList[str] +
				"' width='21' height='21' style='margin: 0 1px;vertical-align: text-bottom'/>"
			);
		}
		if (emojiType.value === 1) {
			if (tvList[str] === undefined) {
				return str;
			}
			return (
				"<img src= '" +
				tvList[str] +
				"' width='21' height='21' style='margin: 0 1px;vertical-align: text-bottom'/>"
			);
		}
		return str;
	});
	addComment(commentForm.value).then(() => {
		sendActive.value = false;
		content.value = "";
		if (blog.blogInfo.siteConfig.commentCheck) {
			window.$message?.warning("评论成功，正在审核中");
		} else {
			window.$message?.success("评论成功");
		}
		// 重新加载评论列表
		emit("reload");
	});
};
const setReply = (flag) => {
	show.value = flag;
};
defineExpose({commentForm, nickname, setReply});
</script>

<style scoped></style>
