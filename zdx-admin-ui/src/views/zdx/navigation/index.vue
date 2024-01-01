<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import {computed, onMounted, reactive, ref} from "vue";
import {page, batchDel, save} from '@/api/base'
import {ElMessage, ElMessageBox} from "element-plus";
import {getToken} from "@/utils/auth";
import {useDict} from "@/utils/dict";

const {zdx_navigation_group} = useDict('zdx_navigation_group')

const queryParams = reactive({
	params: {
		name: ''
	},
	page: 1,
	limit: 10
})

const rules = reactive({
	name: [{ required: true, message: "导航名不能为空", trigger: "blur" }, { min: 2, max: 20, message: "导航名长度必须介于 2 和 20 之间", trigger: "blur" }],
	avatar: [{required: true, message: "头像不能为空", trigger: "blur"}],
	group: [{required: true, message: "分组不能为空", trigger: "blur"}],
	url: [{required: true, message: "链接不能为空", trigger: "blur"}]
})

const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken(),
	}
})

const module = ref('navigation')
const queryRef = ref()
const showSearch = ref(true)
const loading = ref(false)
const total = ref(0)
const navigationList = ref([])
const ids = ref([])
const entity = ref({})
const dialog = ref(false)
const title = ref('')
const formRef = ref()

const pageFriend = () => {
	loading.value = true
	page(module.value, queryParams).then(res => {
		navigationList.value = res.data.records
		loading.value = false
		total.value = parseInt(res.data.total)
	})
}

const resetQuery = (formEl) => {
	if (!formEl) return
	formEl.resetFields()
}

const handleSelectionChange = (selection) => {
	ids.value = selection.map(row => row.id)
	if (selection.length === 1) {
		entity.value = selection[0]
	}
}

const handleAdd = () => {
	dialog.value = true
	title.value = '新增导航'
	entity.value = {}
}

const handleUpdate = (row) => {
	if (!(row instanceof PointerEvent)) {
		if (row) {
			entity.value = row
		}
	}
	dialog.value = true
	title.value = '编辑导航 【' + entity.value.name + '】'
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
			pageFriend()
		})
	})
}

const successHandle = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				save(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					dialog.value = false
					pageFriend()
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}

const handleSuccess = (res) => {
	entity.value.avatar = res.data.fileUrl
}

onMounted(() => {
	pageFriend()
})

</script>

<template>
	<div class="app-container">
		<!--用户数据-->
		<el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="导航名" prop="name">
				<el-input v-model="queryParams.params.name" placeholder="请输入导航名" clearable style="width: 240px"
						  @keyup.enter="pageFriend" @clear="pageFriend"/>
			</el-form-item>
			<el-form-item label="分组" prop="name">
				<el-select v-model="queryParams.params.group" class="m-2" placeholder="请选择" clearable>
					<el-option v-for="item in zdx_navigation_group" :key="item.value" :label="item.value" :value="item.key" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="pageFriend">搜索</el-button>
				<el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="Edit" :disabled="!ids.length > 0" @click="handleUpdate">修改
				</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
						   @click="handleDelete">删除
				</el-button>
			</el-col>
			<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageFriend"
			></zdx-right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="navigationList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="50" align="center"/>
			<el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip/>
			<el-table-column label="导航名" align="center" key="name" prop="name"/>
			<el-table-column label="分组" align="center" key="group" prop="group">
				<template #default="scope">
					<zdx-dict-tag :options="zdx_navigation_group" :value="scope.row.group"/>
				</template>
			</el-table-column>
			<el-table-column label="链接" align="center" key="url" prop="url" show-overflow-tooltip/>
			<el-table-column label="介绍" align="center" key="introduction" prop="introduction" show-overflow-tooltip/>
			<el-table-column label="创建时间" align="center" key="createTime" prop="createTime"/>
			<el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
				<template #default="scope">
					<el-tooltip content="修改" placement="top">
						<el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"/>
					</el-tooltip>
					<el-tooltip content="删除" placement="top">
						<el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)"/>
					</el-tooltip>
				</template>
			</el-table-column>
		</el-table>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page"
						v-model:limit="queryParams.limit"
						@pagination="pageFriend"/>

		<zdx-dialog :dialog="dialog" :title="title" @close="dialog = false">
			<template #content>
				<el-form :model="entity" :rules="rules" ref="formRef" label-width="80px">
					<el-row>
						<el-col :span="12">
							<el-form-item label="导航名" prop="name">
								<el-input v-model="entity.name" clearable placeholder="请输入导航名"/>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="分组" prop="group">
								<el-select v-model="entity.group" class="m-2" placeholder="请选择" clearable>
									<el-option v-for="item in zdx_navigation_group" :key="item.value" :label="item.value" :value="item.key" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="头像" prop="avatar">
								<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
										   :show-file-list="false" accept="image/*"
										   :on-success="handleSuccess">
									<img v-if="entity.avatar" :src="entity.avatar" class="article-cover" />
									<el-icon v-else class="avatar-uploader-icon">
										<Plus />
									</el-icon>
								</el-upload>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="链接" prop="url">
								<el-input v-model="entity.url" clearable placeholder="请输入链接"/>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="介绍" prop="introduction">
								<el-input v-model="entity.introduction" clearable type="textarea"
										  placeholder="请输入介绍"/>
							</el-form-item>
						</el-col>
					</el-row>
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

<style lang="scss" scoped></style>