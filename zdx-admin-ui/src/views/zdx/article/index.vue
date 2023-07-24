<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import {computed, onMounted, reactive, ref} from "vue";
import {batchDel, list, page, save} from "@/api/base";
import {useDict} from "@/utils/dict";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router";
import {batchTrash, batchRecover, batchSync} from "@/api/zdx/article";

const { zdx_article_type } = useDict('zdx_article_type')

const status = ref('all')
const module = ref('article')
const showSearch = ref(true)
const tags = ref([])
const categorys = ref([])
const queryRef = ref()
const loading = ref(false)
const articleList = ref([])
const ids = ref([])
const entity = ref({})
const total = ref(0)

const queryParams = reactive({
	params:{
		trash: false
	},
	page: 1,
	limit: 10
})

const isActive = computed(() => {
	return function (value) {
		return status.value === value ? "active-status" : "status";
	}
})

const changeStatus = (item) => {
	switch (item) {
		case "all":
			queryParams.params.trash = false;
			queryParams.params.status = undefined;
			break;
		case "public":
			queryParams.params.trash = false;
			queryParams.params.status = 'PUBLICITY';
			break;
		case "secret":
			queryParams.params.trash = false;
			queryParams.params.status = 'PRIVACY';
			break;
		case "draft":
			queryParams.params.trash = false;
			queryParams.params.status = 'DRAFT';
			break;
		case "delete":
			queryParams.params.trash = true;
			queryParams.params.status = undefined;
			break;
	}
	status.value = item;
	pageArticle()
}

const resetQuery = (formEl) => {
	if (!formEl) return
	formEl.resetFields()
}
const pageArticle = () => {
	loading.value = true
  page(module.value, queryParams).then(res => {
	  articleList.value = res.data.records
	  loading.value = false
	  total.value = parseInt(res.data.total)
  })
}

const getTags = () => {
  	list('tag').then((res) => {
		  tags.value = res.data
	})
}
const getCategorys = () => {
  list('category').then((res) => {
	  categorys.value = res.data
  })
}

const handleSelectionChange = (selection) => {
	ids.value = selection.map(row => row.id)
	if (selection.length === 1) {
		entity.value = selection[0]
	}
}
const handleChange = (row) => {
  save(module.value, row).then(res => {
	  ElMessage.success(res.message)
	  pageArticle()
  })
}
const handleAdd = () => {
  router.push({path:'/add-article'})
}

const handleUpdate = (row) => {
	router.push(`/add-article/${row.id}`)
}

const handleDelete = (id) => {
	ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		let array = []
		if (typeof id === 'string' && id) {
			array.push(id)
		}
		if (ids.value.length !== 0) {
			array = ids.value
		}
		batchDel(module.value, array).then((res) => {
			ElMessage.success(res.message);
			pageArticle()
		})
	})
}

const handleTrash = (id) => {
	ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		let array = []
		if (typeof id === 'string' && id) {
			array.push(id)
		}
		if (ids.value.length !== 0) {
			array = ids.value
		}
		batchTrash(array).then((res) => {
			ElMessage.success(res.message);
			pageArticle()
		})
	})
}
const handleSync = () => {
	let array = []
	if (ids.value.length !== 0) {
		array = ids.value
	}
	batchSync(array).then((res) => {
		ElMessage.success(res.message);
		pageArticle()
	})
}
const handleRecover = () => {
	ElMessageBox.confirm('确定批量恢复数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		let array = []
		if (ids.value.length !== 0) {
			array = ids.value
		}
		batchRecover(array).then((res) => {
			ElMessage.success(res.message);
			pageArticle()
		})
	})
}
onMounted(() => {
	pageArticle()
})
</script>

<template>
    <div class="app-container">
		<!-- 文章状态 -->
		<el-row :gutter="24" style="color: #999;" class="mb15">
			<el-col :span="1.5">
				状态
			</el-col>
			<el-col :span="1.5" :class="isActive('all')" @click="changeStatus('all')">
				全部
			</el-col>
			<el-col :span="1.5" :class="isActive('public')" @click="changeStatus('public')">
				公开
			</el-col>
			<el-col :span="1.5" :class="isActive('secret')" @click="changeStatus('secret')">
				私密
			</el-col>
			<el-col :span="1.5" :class="isActive('draft')" @click="changeStatus('draft')">
				草稿
			</el-col>
			<el-col :span="1.5" :class="isActive('delete')" @click="changeStatus('delete')">
				回收站
			</el-col>
		</el-row>
		<el-divider />
		<!-- 搜索栏 -->
		<el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="名称">
				<el-input @keyup.enter="pageArticle" v-model="queryParams.params.keyword"
						  placeholder="请输入文章名称" clearable />
			</el-form-item>
			<el-form-item label="类型">
				<el-select v-model="queryParams.params.articleType" placeholder="请选择" clearable
						   >
					<el-option v-for="item in zdx_article_type" :key="item.value" :label="item.value"
							   :value="item.key"/>
				</el-select>
			</el-form-item>
			<el-form-item label="标签">
				<el-select v-model="queryParams.params.tagId" placeholder="请选择标签" clearable filterable @visible-change="getTags"
						   >
					<el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="分类">
				<el-select v-model="queryParams.params.categoryId" placeholder="请选择分类" clearable filterable
						   @visible-change="getCategorys">
					<el-option v-for="item in categorys" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="pageArticle">搜索</el-button>
				<el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5" v-if="status !== 'delete'">
				<el-button type="primary"  plain icon="Plus" @click="handleAdd">新增</el-button>
			</el-col>
			<el-col :span="1.5" v-if="status !== 'delete'">
				<el-button type="success"  plain icon="Edit" :disabled="!ids.length > 0" @click="handleUpdate">修改
				</el-button>
			</el-col>
			<el-col :span="1.5" v-if="status !== 'delete'">
				<el-button type="success"  plain icon="Position" :disabled="!ids.length > 0" @click="handleSync">批量同步es
				</el-button>
			</el-col>
			<el-col :span="1.5" v-if="status === 'delete'">
				<el-button v-checkRole="'use_blog'" type="success" plain icon="Edit" :disabled="!ids.length > 0" @click="handleRecover">批量恢复
				</el-button>
			</el-col>
			<el-col :span="1.5" v-if="status !== 'delete'">
				<el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
						   @click="handleTrash" v-checkRole="'use_blog'">删除
				</el-button>
			</el-col>
			<el-col :span="1.5" v-if="status === 'delete'">
				<el-button type="danger"  plain icon="Delete" :disabled="!ids.length > 0"
						   @click="handleDelete" v-checkRole="'use_blog'">彻底删除
				</el-button>
			</el-col>
			<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageArticle"
			></zdx-right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="50" align="center"/>
			<el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip/>
			<el-table-column label="标题" align="center" key="title" prop="title" show-overflow-tooltip/>
			<el-table-column label="分类" align="center" key="categoryName" prop="categoryName" show-overflow-tooltip />
			<el-table-column label="浏览量" align="center" key="viewCount" prop="viewCount" show-overflow-tooltip />
			<el-table-column label="点赞量" align="center" key="likeCount" prop="likeCount" show-overflow-tooltip />
			<el-table-column label="置顶" align="center" key="isTop" prop="isTop" >
				<template #default="scope">
					<el-switch v-model="scope.row.isTop" class="ml-2" :active-value="true" :inactive-value="false"
							   style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
							   @change="handleChange(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="推荐" align="center" key="isRecommend" prop="isRecommend" >
				<template #default="scope">
					<el-switch v-model="scope.row.isRecommend" class="ml-2" :active-value="true" :inactive-value="false"
							   style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
							   @change="handleChange(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="创建时间" align="center" key="createTime" prop="createTime" show-overflow-tooltip/>
			<el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
				<template #default="scope">
					<el-tooltip content="修改" placement="top">
						<el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"/>
					</el-tooltip>
					<el-tooltip content="删除" placement="top" v-checkRole="'use_blog'">
						<el-button link type="primary" icon="Delete" @click="handleTrash(scope.row.id)"/>
					</el-tooltip>
				</template>
			</el-table-column>
		</el-table>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page"
						v-model:limit="queryParams.limit"
						@pagination="pageArticle"/>
    </div>
</template>

<style lang="scss" scoped>
.status {
	cursor: pointer;
}

.active-status {
	cursor: pointer;
	color: #333;
	font-weight: bold;
}

.article-cover {
	position: relative;
	width: 100%;
	height: 90px;
	border-radius: 4px;
}

.article-cover::after {
	content: "";
	background: rgba(0, 0, 0, 0.3);
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
}

.article-status-icon {
	color: #fff;
	font-size: 1.5rem;
	position: absolute;
	right: 1rem;
	bottom: 1.4rem;
}
</style>