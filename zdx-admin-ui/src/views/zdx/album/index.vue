<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"

import {computed, onMounted, reactive, ref} from "vue";
import {del, page, save} from "@/api/base";
import router from "@/router";
import {ElMessage, ElMessageBox} from "element-plus";
import {getToken} from "@/utils/auth";

const queryParams = reactive({
	params: {

	},
	page: 1,
	limit: 10
})

const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken(),
	}
})


const rules = reactive({
	albumName: [{required: true, message: "相册名不能为空", trigger: "blur"}],
	albumCover: [{required: true, message: "相册封面不能为空", trigger: "blur"}],
})

const showSearch = ref(true)
const module = ref('album')
const albumList = ref([])
const loading = ref(false)
const total = ref(0)
const dialog = ref(false)
const title = ref('')
const entity = ref({})
const formRef = ref()

const pageAlbum = () => {
  page(module.value, queryParams).then(res => {
	  loading.value = true
	  page(module.value, queryParams).then(res => {
		  albumList.value = res.data.records
		  loading.value = false
		  total.value = parseInt(res.data.total)
	  })
  })
}
const checkPhoto = (id) => {
	router.push({ path: `/photo/${id}` });
}

const handleUpdate = (row) => {
	dialog.value = true
	title.value = '更改相册【' + row.albumName + '】'
	entity.value = row
}
const handleAdd = () => {
	dialog.value = true
	title.value = '新增相册'
	entity.value = {}
}
const handleDelete = (id) => {
	ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		del(module, id).then((res) => {
			ElMessage.success(res.message)
			pageAlbum()
		})
	})
}
const handleSuccess = (response) => {
	entity.value.albumCover = response.data.fileUrl;
}

const successHandle = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				save(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					dialog.value = false
					pageAlbum()
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}

onMounted(() => {
	pageAlbum()
})

</script>

<template>
    <div  class="app-container">
		<!-- 搜索栏 -->
		<el-form @submit.native.prevent :inline="true" v-show="showSearch">
			<el-form-item label="相册名称">
				<el-input v-model="queryParams.keyword" style="width: 200px" placeholder="请输入相册名称" clearable
						  @keyup.enter="pageAlbum" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="pageAlbum">搜索</el-button>
			</el-form-item>
		</el-form>
		<!-- 操作按钮 -->
		<el-row :gutter="10" class="mb15">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Plus" @click="handleAdd">新建</el-button>
			</el-col>
			<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageAlbum"></zdx-right-toolbar>
		</el-row>

		<!-- 相册列表 -->
		<el-row :gutter="12" v-loading="loading" style="padding-top: 20px">
			<el-empty v-if="albumList === null" description="暂无相册" />
			<el-col v-for="album of albumList" :key="album.id" :xs="12" :sm="12" :lg="6">
				<div class="album-item" @click="checkPhoto(album.id)">
					<div class="album-operation">
						<el-dropdown>
							<el-icon style="color:#fff">
								<MoreFilled />
							</el-icon>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="handleUpdate(album)">编辑</el-dropdown-item>
									<el-dropdown-item @click="handleDelete(album.id)">删除</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
					<el-image class="album-cover" fit="cover" :src="album.albumCover">
					</el-image>
					<div class="photo-count">
						<div>{{ album.photoCount }}</div>
						<el-icon v-if="album.status">
							<Hide />
						</el-icon>
					</div>
					<div class="album-name">{{ album.albumName }}</div>
				</div>
			</el-col>
		</el-row>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
						@pagination="pageAlbum" />

		<zdx-dialog :title="title" :dialog="dialog" @close="dialog = false">
			<template #content>
				<el-form :model="entity" :rules="rules" ref="formRef" label-width="80px">
					<el-form-item label="相册名" prop="albumName">
						<el-input v-model="entity.albumName" placeholder="请输入相册名" clearable />
					</el-form-item>
					<el-form-item label="相册封面" prop="albumCover">
						<el-upload style="width: 70%" drag :show-file-list="false" :headers="authorization"
								   action="/api/zdx.file/upload"
								   accept="image/*" :on-success="handleSuccess">
							<el-icon class="el-icon--upload" v-if="entity.articleCover === undefined">
								<upload-filled/>
							</el-icon>
							<div class="el-upload__text" v-if="entity.articleCover === undefined">
								将文件拖到此处，或<em>点击上传</em>
							</div>
							<img v-else :src="entity.articleCover" width="360" alt=""/>
						</el-upload>
					</el-form-item>
					<el-form-item label="状态" prop="status">
						<el-radio-group v-model="entity.status">
							<el-radio :label="true">是</el-radio>
							<el-radio :label="false">否</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="备注">
						<el-input v-model="entity.description" type="textarea" placeholder="请输入内容"></el-input>
					</el-form-item>
				</el-form>
			</template>
			<template #footer>
				<el-button type="primary" @click="successHandle(formRef)">
					保存
				</el-button>
			</template>
		</zdx-dialog>
    </div>
</template>

<style lang="scss" scoped>
.album-item {
	position: relative;
	cursor: pointer;
	margin-bottom: 1rem;

	.album-operation {
		position: absolute;
		top: 0.5rem;
		right: 0.8rem;
		z-index: 9;
	}

	.album-cover {
		position: relative;
		border-radius: 4px;
		width: 100%;
		height: 170px;

		&:before {
			content: "";
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			background: rgba(0, 0, 0, 0.5);
		}
	}

	.photo-count {
		position: absolute;
		left: 0;
		right: 0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 1.5rem;
		padding: 0 0.5rem;
		bottom: 2.6rem;
		color: #fff;
	}

	.album-name {
		text-align: center;
		margin-top: 0.5rem;
	}
}
</style>