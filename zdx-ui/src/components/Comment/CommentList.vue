<template>
	<div class="reply-warp" id="reply-wrap">
		<div class="reply-title">
			<svg-icon icon-class="comment" size="1.4rem" style="margin-right: 5px;"></svg-icon>
			评论
		</div>
		<ReplyBox @reload="reloadComments" :comment-type="commentType" :type-id="typeId"></ReplyBox>
		<div v-if="count > 0 && reFresh">
			<div class="reply-item" v-for="(comment, index) of commentList" :key="comment.id">
				<div class="reply-box-avatar">
					<img class="shoka-avatar" :src="comment.avatar">
				</div>
				<div class="content-warp">
					<div class="user-info">
						<div class="user-name">{{ comment.fromNickname }}</div>
						<svg-icon v-if="comment.fromUid === 1" icon-class="badge"></svg-icon>
					</div>
					<div class="reply-content" v-html="comment.content"></div>
					<div class="reply-info">
						<span class="reply-time">{{ comment.createTime }}</span>
						<span class="reply-like" @click="like(comment)">
              <svg-icon class="like" icon-class="like" size="0.8rem" :class="isLike(comment.id)"
						style="margin-right: 5px"></svg-icon>
              <span v-show="comment.likeCount">{{ comment.likeCount }}</span>
            </span>
						<span class="reply-btn" @click="handleReply(index, comment)">回复</span>
					</div>
					<div class="sub-reply-item" v-for="reply of comment.replyVoList" :key="reply.id">
						<div class="sub-user-info">
							<img class="sub-reply-avatar" :src="reply.avatar"/>
							<div class="sub-user-name">{{ reply.fromNickname }}</div>
							<svg-icon v-if="reply.fromUid === 1" icon-class="badge"
									  style="margin-left: 5px;"></svg-icon>
						</div>
						<span class="reply-content">
              <template v-if="reply.fromUid !== reply.toUid">回复 <span style="color: #008ac5">@{{
					  reply.toNickname
				  }}</span> :</template>
              <span v-html="reply.content"></span>
            </span>
						<div class="reply-info">
							<span class="reply-time">{{ reply.createTime }}</span>
							<span class="reply-like" @click="like(reply)">
                <svg-icon class="like" icon-class="like" size="0.8rem" :class="isLike(reply.id)"
						  style="margin-right: 5px"></svg-icon>
                <span v-show="reply.likeCount > 0">{{ reply.likeCount }}</span>
              </span>
							<span class="reply-btn" @click="handleReply(index, reply)">回复</span>
						</div>
					</div>
					<div ref="readMoreRef" class="view-more" v-show="comment.replyCount > 3">
						<span>共{{ comment.replyCount }}条回复, </span>
						<span class="view-more-btn" @click="readMoreComment(index, comment)">点击查看</span>
					</div>
					<Paging ref="pageRef" :total="comment.replyCount" :index="index" :commentId="comment.id"
							@get-current-page="getCurrentPage"></Paging>
					<ReplyBox ref="replyRef" class="mt-4" :show="false" :comment-type="commentType" :type-id="typeId"
							  @reload="reloadReplies(index)">
					</ReplyBox>
					<div class="bottom-line"></div>
				</div>
			</div>
			<div class="loading-warp" v-if="count > commentList.length">
				<n-button class="btn" color="#e9546b" @click="getList">
					加载更多...
				</n-button>
			</div>
		</div>
		<div v-else style="padding: 1.25rem; text-align: center">来发评论吧~</div>
	</div>
</template>

<script setup>
import {getReplyPage, pageHomeComment, likeComment} from '@/api/comment'
import ReplyBox from "@/components/Comment/ReplyBox.vue";
import Paging from "@/components/Pagination/Paging.vue";
import useStore from "@/stores";
import {computed, nextTick, onMounted, reactive, ref, toRefs, watch} from "vue";
import {useRoute} from "vue-router";

const {user, app} = useStore();
const replyRef = ref([]);
const pageRef = ref([]);
const readMoreRef = ref([]);
const props = defineProps({
	commentType: {
		type: String,
	}
});
const emit = defineEmits(["getCommentCount"]);
const typeId = computed(() => useRoute().params.id) ? useRoute().params.id : undefined;
const isLike = computed(() => (id) => user.commentLikeSet.indexOf(id) !== -1 ? "like-flag" : "");
const data = reactive({
	count: 0,
	reFresh: true,
	queryParams: {
		page: 1,
		limit: 10,
		params: {
			typeId: typeId,
			commentType: props.commentType
		}
	},
	commentList: [],
});
const {count, reFresh, queryParams, commentList} = toRefs(data);
const like = (comment) => {
	if (!user.id) {
		app.setLoginFlag(true);
		return;
	}
	let id = comment.id;
	likeComment(id).then(() => {
		//判断是否点赞
		if (user.commentLikeSet.indexOf(id) !== -1) {
			comment.likeCount -= 1;
		} else {
			comment.likeCount += 1;
		}
		user.commentLike(id);
	});
};
// 刷新评论列表
watch(
	commentList,
	() => {
		reFresh.value = false;
		nextTick(() => {
			reFresh.value = true
		})
	},
	{deep: false},
);
// 查看更多评论
const readMoreComment = (index, comment) => {
	getReplyPage({
		params: {
			commentId: comment.id
		},
		page: 1,
		limit: 5,
	}).then((res) => {
		comment.replyVoList = res.data.records;
		// 回复大于5条展示分页
		if (comment.replyCount > 5) {
			pageRef.value[index].setPaging(true);
		}
		// 隐藏查看更多
		readMoreRef.value[index].style.display = "none";
	});
};
// 查看当前页的回复评论
const getCurrentPage = (current, index, commentId) => {
	getReplyPage({
		params: {
			commentId: commentId
		},
		page: current,
		limit: 5,
	}).then((res) => {
		commentList.value[index].replyVoList = res.data.records;
	});
};
const handleReply = (index, target) => {
	replyRef.value.forEach((element) => {
		element.setReply(false);
	});
	const currentReply = replyRef.value[index];
	currentReply.nickname = target.fromNickname;
	currentReply.commentForm.replyId = target.id;
	currentReply.commentForm.toUid = target.fromUid;
	currentReply.commentForm.parentId = commentList.value[index].id;
	currentReply.setReply(true);
};
const getList = () => {
	pageHomeComment(queryParams.value).then(res => {
		if (queryParams.value.page === 1) {
			commentList.value = res.data.records
		} else {
			commentList.value.push(...res.data.records)
		}
		queryParams.value.page++;
		count.value = parseInt(res.data.total)
		emit('getCommentCount', count.value)
	})
};
// 重新加载评论列表
const reloadComments = () => {
	queryParams.value.page = 1;
	getList();
};
// 重新加载回复评论
const reloadReplies = (index) => {
	getReplyPage({
		params: {
			commentId: commentList.value[index].id
		},
		page: pageRef.value[index].current,
		limit: 5,
	}).then((res) => {
		commentList.value[index].replyVoList = res.data.records;
		commentList.value[index].replyCount++;
		// 隐藏回复框
		replyRef.value[index].setReply(false);
		// 回复大于5条展示分页
		if (commentList.value[index].replyCount > 5) {
			pageRef.value[index].setPaging(true);
		}
		// 隐藏查看更多
		readMoreRef.value[index].style.display = "none";
	});
};
onMounted(() => {
	getList();
});
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";

.reply-title {
	display: flex;
	align-items: center;
	margin: 22px 0;
	padding-left: 10px;
	font-size: 20px;
	font-weight: 600;
	line-height: 40px;
}

.sub-reply-avatar {
	position: absolute;
	left: 0;
	width: 1.5rem;
	height: 1.5rem;
	border-radius: 50%;
}

.reply-item {
	display: flex;
	padding-top: 1rem;

	.content-warp {
		flex: auto;
		margin-left: 0.6rem;
	}

	.bottom-line {
		border-bottom: 1px solid var(--grey-3);
		margin-top: 0.5rem;
	}
}

.user-info {
	display: flex;
	align-items: center;
	margin-bottom: 4px;

	.user-name {
		font-size: 0.875rem;
		font-weight: 500;
		margin-right: 0.3125rem;
	}
}

.sub-reply-item {
	position: relative;
	padding: 8px 0 8px 33px;
	font-size: 15px;
	line-height: 24px;

	.sub-user-info {
		display: inline-flex;
		align-items: center;
		margin-right: 9px;
		line-height: 24px;
	}

	.sub-user-name {
		font-size: 13px;
		line-height: 24px;
	}
}

.reply-info {
	display: flex;
	align-items: center;
	margin-top: 0.125rem;
	font-size: 0.8125rem;
	color: #9499a0;

	.reply-time {
		margin-right: 15px;
		padding-top: 2px;
	}

	.reply-like {
		display: flex;
		align-items: center;
		margin-right: 17px;
		cursor: pointer;

		&:hover .like {
			color: var(--color-pink);
		}
	}

	.reply-btn {
		cursor: pointer;

		&:hover {
			color: var(--color-pink);
		}
	}
}

.reply-content {
	overflow: hidden;
	word-wrap: break-word;
	word-break: break-word;
	white-space: pre-wrap;
	font-size: 0.9375rem;
	line-height: 1.5;
	vertical-align: baseline;
}

.view-more {
	font-size: 13px;
	color: #9499a0;

	.view-more-btn {
		cursor: pointer;

		&:hover {
			color: var(--color-pink);
		}
	}
}
</style>