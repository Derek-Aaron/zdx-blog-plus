<template>
	<div class="page-header">
		<h1 class="page-title">分类</h1>
		<img class="page-cover"
			 src="https://static.zhaodengxuan.top/config/1925201pp2P.jpg"
			 alt="">
		<Waves></Waves>
	</div>
	<div class="bg">
		<div class="page-container">
			<n-grid x-gap="15" y-gap="15" cols="1 s:2 m:3" responsive="screen">
				<n-grid-item class="article-item" v-for="article of articleList" :key="article.id">
					<div class="article-cover">
						<router-link :to="`/article/${article.id}`"><img class="cover" v-lazy="article.cover">
						</router-link>
					</div>
					<div class="article-info">
						<div class="article-title">
							<router-link :to="`/article/${article.id}`">{{ article.title }}</router-link>
						</div>
						<div class="article-meta">
              <span><svg-icon icon-class="calendar" size="0.95rem"></svg-icon>
                {{ article.createTime }}</span>
							<router-link :to="`/category/${article.category.id}`">
								<svg-icon icon-class="qizhi"
										  size="0.9rem"></svg-icon>
								{{ article.category.name }}
							</router-link>
						</div>
						<div class="tag-info">
							<router-link :to="`/tag/${tag.id}`" class="article-tag" v-for="tag in article.tagVOList"
										 :key="tag.id">
								<svg-icon icon-class="tag" size="0.8rem"></svg-icon>
								{{ tag.name }}
							</router-link>
						</div>
					</div>
				</n-grid-item>
			</n-grid>
			<Pagination v-if="count > 5" v-model:current="queryParams.page" :total="Math.ceil(count / 5)"></Pagination>
		</div>
	</div>
</template>

<script setup>
import {categoryArticlePage} from '@/api/category';
import Waves from "@/components/Waves/index.vue"
import {useRoute} from "vue-router";
import {onMounted, reactive, toRefs} from "vue";
import Pagination from "@/components/Pagination/index.vue";

const route = useRoute();
const data = reactive({
	queryParams: {
		params: {
			categoryId: route.params.categoryId,
		},
		page: 1,
		limit: 5,
	},
	count: 0,
	articleList: []
});
const {queryParams, count, articleList} = toRefs(data);
onMounted(() => {
	categoryArticlePage(queryParams.value).then((res) => {
		articleList.value = res.data.records;
		count.value = parseInt(res.data.total)
	})
})
</script>

<style lang="scss" scoped>
.article-item {
	display: flex;
	flex-direction: column;
	box-shadow: 0 0.625rem 1.875rem -0.9375rem var(--box-bg-shadow);
	transition: all 0.2s ease-in-out 0s;
	animation: zoomIn 1s both;

	&:hover {
		box-shadow: 0 0 2rem var(--box-bg-shadow);
	}
}

.article-cover {
	width: 100%;
	height: 12rem;
	overflow: hidden;
	border-radius: 0.625rem 0.625rem 0 0;
}

.cover {
	object-fit: cover;
	width: 100%;
	height: 100%;
	transition: all .5s;
}

.article-item:hover .cover {
	transform: scale(1.1);
}

.article-info {
	padding: 0.6rem 0.8rem 1rem;
}

.article-title {
	font-size: 23px;
	font-weight: 400;
	line-height: 1.25;
}

.article-meta {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: 0.625rem;
	font-size: 14px;
	color: var(--grey-5);
}
</style>