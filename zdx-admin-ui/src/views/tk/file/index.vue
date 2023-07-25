<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import {getToken} from '@/utils/auth'
import {onMounted, reactive, ref} from "vue";
import {page, batchDel} from "@/api/base"
import {downloadFile, getFileUrl} from "@/api/tk/file"
import {download} from "@/utils/download"
import {useDict} from "@/utils/dict";
import {ElMessage, ElMessageBox, ElLoading} from "element-plus";

const {zdx_file_type} = useDict('zdx_file_type')

const queryParams = reactive({
	params: {
		name: ''
	},
	page: 1,
	limit: 10
})

const columns = reactive([
	{key: 0, label: `编号`, visible: true},
	{key: 1, label: `文件名`, visible: true},
	{key: 2, label: `文件类型`, visible: true},
	{key: 3, label: `文件路径`, visible: true},
	{key: 4, label: `文件大小`, visible: true},
	{key: 5, label: `创建时间`, visible: true},
])

const module = ref('file')
const showSearch = ref(true)
const fileList = ref([])
const loading = ref(false)
const dialog = ref(false)
const openView = ref(false)
const total = ref(0)
const queryRef = ref()
const ids = ref([])
const entity = ref({})

const resetQuery = (formEl) => {
	if (!formEl) return
	formEl.resetFields()
}

const pageFile = () => {
	loading.value = true
	page(module.value, queryParams).then(res => {
		fileList.value = res.data.records
		loading.value = false
		total.value = parseInt(res.data.total)
	})
}

const handleSelectionChange = (selection) => {
	console.log(selection);
	ids.value = selection.map(row => row.id)
	if (selection.length === 1) {
		entity.value = selection[0]
	}
}

const handleAdd = () => {
	dialog.value = true
}

const uploadSuccess = (res => {
	ElMessage.success(res.message)
	dialog.value = false
	pageFile()
})

const uploadError = (err) => {
	console.log(err);
	ElMessage.error('文件上传失败')
}

const handleDownload = (row) => {
	const downloadLoadingInstance = ElLoading.service({text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)",})
	downloadFile(row.id).then(res => {
		download(res, row.name)
		downloadLoadingInstance.close()
	}).catch(err => {
		ElMessage.error('下载文件异常')
		downloadLoadingInstance.close()
	})
}

const handleView = (row) => {
	if (row) {
		entity.value = row
	}
	openView.value = true

	getFileUrl(row.id).then(res => {
		entity.value.url = res.data.fileUrl
	})
}

const handleDelete = (id) => {
	ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		let array = []
		console.log(id);
		if (typeof id === 'string' && id) {
			array.push(id)
		}
		if (ids.value.length !== 0) {
			array = ids.value
		}
		batchDel(module.value, array).then((res) => {
			ElMessage.success(res.message);
			pageFile()
		})
	})
}

onMounted(() => {
	pageFile()
})
</script>

<template>
	<div class="app-container">
		<!--用户数据-->
		<el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="角色名" prop="name">
				<el-input v-model="queryParams.params.name" placeholder="请输入角色名" clearable style="width: 240px"
						  @keyup.enter="pageRole" @clear="pageRole"/>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="pageFile">搜索</el-button>
				<el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="UploadFilled" @click="handleAdd">上传</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
						   @click="handleDelete">删除
				</el-button>
			</el-col>
			<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageFile"
							   :columns="columns"></zdx-right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="fileList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="50" align="center"/>
			<el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip
							 v-if="columns[0].visible"/>
			<el-table-column label="文件名" align="center" key="name" prop="name" show-overflow-tooltip
							 v-if="columns[1].visible"/>
			<el-table-column label="文件类型" align="center" key="bucketName" prop="bucketName"
							 v-if="columns[2].visible">
				<template #default="scope">
					<zdx-dict-tag :options="zdx_file_type" :value="scope.row.bucketName"/>
				</template>
			</el-table-column>
			<el-table-column label="文件路径" align="center" key="bucketName" prop="bucketName" show-overflow-tooltip
							 v-if="columns[3].visible"/>
			<el-table-column label="文件大小" align="center" key="size" prop="size" show-overflow-tooltip
							 v-if="columns[4].visible"/>
			<el-table-column label="创建时间" align="center" key="createTime" prop="createTime" show-overflow-tooltip
							 v-if="columns[5].visible"/>
			<el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
				<template #default="scope">
					<el-tooltip content="在线预览" placement="top">
						<el-button link type="primary" icon="View" @click="handleView(scope.row)"/>
					</el-tooltip>
					<el-tooltip content="下载文件" placement="top">
						<el-button link type="primary" icon="Download" @click="handleDownload(scope.row)"/>
					</el-tooltip>
					<el-tooltip content="删除" placement="top">
						<el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)"/>
					</el-tooltip>
				</template>
			</el-table-column>
		</el-table>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page"
						v-model:limit="queryParams.limit"
						@pagination="pageFile"/>

		<zdx-dialog :dialog="dialog" title="上传文件" @close="dialog = false">
			<template #content>
				<el-form :model="entity" label-width="80px">
					<el-form-item label="文件路径" prop="bucketName" >
						<el-select style="width: 100%" v-model="entity.bucketName" class="m-2" placeholder="请选择" clearable>
							<el-option v-for="item in zdx_file_type" :key="item.key" :label="item.value"
									   :value="item.key" />
						</el-select>
					</el-form-item>
					<el-form-item label="上传文件" prop="file" style="width: 100%">
						<el-upload style="width: 100%" class="upload-demo" drag action="/api/zdx.file/upload" :headers="{
                    Authorization: 'Bearer ' + getToken(),
                }" :data="entity.bucketName ? {type: entity.bucketName} : {}" :on-success="uploadSuccess" :on-error="uploadError" multiple>
							<el-icon class="el-icon--upload">
								<upload-filled/>
							</el-icon>
							<div class="el-upload__text">
								请选择或者拖转文件
							</div>
						</el-upload>
					</el-form-item>
				</el-form>

			</template>
		</zdx-dialog>
		<zdx-dialog :dialog="openView" title="在线预览文件" @close="openView = false">
			<template #content>
				<el-image v-if="entity.bucketName === 'IMAGE'" :src="entity.url"/>
			</template>
		</zdx-dialog>
	</div>
</template>

<style scoped></style>