<template>
	<div class="page-header">
		<h1 class="page-title">开发导航</h1>
		<img class="page-cover" src="https://static.zhaodengxuan.top/config/navigation.jpg" alt="" />
		<!-- 波浪 -->
		<Waves></Waves>
	</div>
	<div class="bg">
		<div class="page-container" v-for="item in navigationList">
			<h2>
				<svg-icon class="flower" icon-class="flower" size="1.25rem" color="pink"></svg-icon>
				{{ item.name }}
			</h2>
			<div class="friends">
				<div class="friend-item" v-animate="['slideUpBigIn']" v-for="friend in item.list" :key="friend.id">
					<a target="_blank" :href="friend.url">
						<img class="image" v-lazy="friend.avatar" />
					</a>
					<div class="info">
						<a class="name" target="_blank" :href="friend.url">{{
								friend.name
							}}</a>
						<p class="desc">{{ friend.introduction }}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import Waves from "@/components/Waves/index.vue"
import useStore from "@/stores";
import {onMounted, ref} from "vue";
import {group} from "@/api/navigation";
const { blog } = useStore();
const navigationList = ref([]);
onMounted(() => {
	group().then((res) => {
		if (res.data) {
			navigationList.value = res.data
		}
	})
});
</script>

<style lang="scss" scoped>
.block {
	line-height: 2;
	margin: 0 1.5rem;
	font-size: 15px;
	border-left: 0.2rem solid var(--color-purple);
	padding: 0.625rem 1rem;
	color: var(--grey-5);
	background: var(--color-pink-light);
	border-radius: 4px;
	word-wrap: break-word;
}

.welcome {
	position: relative;
	margin: 0 2.5rem 0.5rem;

	&::before {
		content: "";
		position: absolute;
		width: 0.4em;
		height: 0.4em;
		background: var(--primary-color);
		border-radius: 50%;
		top: 0.85em;
		left: -1em;
	}
}

.friends {
	display: flex;
	flex-wrap: wrap;
	font-size: 0.9rem;
}

.friend-item {
	display: inline-flex;
	align-items: center;
	width: calc(50% - 2rem);
	margin: 1rem;
	padding: 0.5rem 1rem;
	line-height: 1.5;
	border-radius: 0.5rem;
	border: 0.0625rem solid var(--grey-2);
	box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
	transition: all 0.2s ease-in-out 0s;
	animation-duration: 0.5s;
	visibility: hidden;

	&:hover {
		box-shadow: 0 0 2rem var(--box-bg-shadow);
	}

	.image {
		display: block;
		width: 4rem;
		height: 4rem;
		border-radius: 0.9375rem;
	}

	.info {
		padding-left: 1rem;
	}

	.name {
		font-weight: 700;
	}

	.desc {
		font-size: 0.75em;
		margin: 0.5rem 0;
	}
}

.flower {
	animation: rotate 6s linear infinite;
}

@keyframes rotate {
	0% {
		transform: rotate(0);
	}

	100% {
		transform: rotate(360deg);
	}
}

@media (max-width: 767px) {
	.friend-item {
		width: calc(100% - 2rem);
	}
}
</style>
