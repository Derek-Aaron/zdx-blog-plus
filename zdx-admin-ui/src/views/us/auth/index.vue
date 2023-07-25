<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import zdxIconSelect from "@/components/IconSelect/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, batchDel, save } from '@/api/base'
import { ElMessage, ElMessageBox } from "element-plus";
import {useDict} from "@/utils/dict";

const { zdx_auth_source } = useDict('zdx_auth_source')

const module = ref('auth')
const authRef = ref()
const authList = ref([])
const showChooseIcon = ref(false)
const loading = ref(false)
const dialog = ref(false)
const title = ref('')
const total = ref(0)
const showSearch = ref(true)
const queryRef = ref()
const iconSelectRef = ref()
const ids = ref([])
const entity = ref({})


const queryParams = reactive({
	params: {
		name: ''
	},
	page: 1,
	limit: 10
})

const columns = reactive([
	{ key: 0, label: `编号`, visible: true },
	{ key: 1, label: `角色`, visible: true },
	{ key: 2, label: `角色名`, visible: true },
])

const rules = reactive({
	clientId: [{ required: true, message: "key不能为空", trigger: "blur" }],
	source: [{ required: true, message: "来源不能为空", trigger: "blur" }],
	secret: [{ required: true, message: "密匙不能为空", trigger: "blur" }],
	icon: [{ required: true, message: "图标不能为空", trigger: "blur" }],
	callback: [{ required: true, message: "回调地址不能为空", trigger: "blur" }],
	type: [{ required: true, message: "类型不能为空", trigger: "blur" }]
})


const pageAuth = () => {
	loading.value = true
	page(module.value, queryParams).then(res => {
		authList.value = res.data.records
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
	title.value = '新增角色'
	entity.value = {}
}

const handleUpdate = (row) => {
	if (!(row instanceof PointerEvent)) {
		if (row) {
			entity.value = row
		}
	}
	dialog.value = true
	title.value = '编辑角色 【' + entity.value.username + '】'
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
			pageAuth()
		})
	})
}

const handleChange = (row) => {
  save(module.value, row).then(res => {
	  ElMessage.success(res.message)
	  pageAuth()
  })
}

const selected = (name) => {
	entity.value.icon = name;
	showChooseIcon.value = false;
}

const showSelectIcon = () => {
	iconSelectRef.value.reset();
	showChooseIcon.value = true;
}

const hideSelectIcon = (event) => {
	let elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget;
	let className = elem.className;
	if (className !== "el-input__inner") {
		showChooseIcon.value = false;
	}
}

const successHandle = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				save(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					dialog.value = false
					pageAuth()
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}

onMounted(() => {
	pageAuth()
})
</script>

<template>
	<div class="app-container">
		<!--用户数据-->
		<el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="角色名" prop="name">
				<el-input v-model="queryParams.params.name" placeholder="请输入角色名" clearable style="width: 240px"
						  @keyup.enter="pageAuth" @clear="pageAuth" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="pageAuth">搜索</el-button>
				<el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="Edit" :disabled="!ids.length > 0" @click="handleUpdate">修改</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
						   @click="handleDelete">删除</el-button>
			</el-col>
			<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageAuth"
							   :columns="columns"></zdx-right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="authList" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="50" align="center" />
			<el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
			<el-table-column label="用户名" align="center" key="username" prop="username" show-overflow-tooltip v-if="columns[1].visible" />
			<el-table-column label="用户名" align="center" key="username" prop="username" show-overflow-tooltip>
				<template #default="scope">
					{{scope.row.type === 'home' ? '前台':'后台'}}
				</template>
			</el-table-column>
			<el-table-column label="来源" align="center" key="source" prop="source" show-overflow-tooltip v-if="columns[2].visible">
				<template #default="scope">
					<zdx-dict-tag :options="zdx_auth_source" :value="scope.row.source" />
				</template>
			</el-table-column>
			<el-table-column label="key" align="center" key="clientId" prop="clientId" show-overflow-tooltip v-if="columns[1].visible" />
			<el-table-column label="secret" align="center" key="secret" prop="secret" show-overflow-tooltip v-if="columns[1].visible" />
			<el-table-column label="图标" align="center" key="icon" prop="icon" show-overflow-tooltip v-if="columns[1].visible" >
				<template #default="scope">
					<svg-icon :icon-class="scope.row.icon" />
				</template>
			</el-table-column>
			<el-table-column label="是否禁用" align="center" key="isEnabled" prop="isEnabled" show-overflow-tooltip v-if="columns[1].visible" >
				<template #default="scope">
					<el-switch v-model="scope.row.isEnabled" class="ml-2" :active-value="false" :inactive-value="true"
							   style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
							   @change="handleChange(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
				<template #default="scope">
					<el-tooltip content="修改" placement="top">
						<el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
					</el-tooltip>
					<el-tooltip content="删除" placement="top">
						<el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)" />
					</el-tooltip>
				</template>
			</el-table-column>
		</el-table>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
						@pagination="pageAuth" />

		<zdxDialog :dialog="dialog" :title="title" @close="dialog = false">
			<template #content>
				<el-form :model="entity" :rules="rules" ref="authRef" label-width="80px">
					<el-row>
						<el-col :span="24">
							<el-form-item label="key" prop="clientId">
								<el-input v-model="entity.clientId" placeholder="请输入key" clearable />
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="密匙" prop="secret">
								<el-input v-model="entity.secret" placeholder="请输入密匙" clearable />
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="回调地址" prop="callback">
								<el-input v-model="entity.callback" placeholder="请输入回调地址" clearable />
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="类型" prop="type">
								<el-select v-model="entity.type" class="m-2" placeholder="请选择" clearable>
									<el-option label="前台" value="home"></el-option>
									<el-option label="后台" value="admin"></el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="来源" prop="source">
								<el-select v-model="entity.source" class="m-2" placeholder="请选择" clearable>
									<el-option v-for="item in zdx_auth_source" :key="item.value" :label="item.value" :value="item.key" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="图标" prop="icon">
								<el-popover placement="bottom-start" :width="540" v-model:visible="showChooseIcon"
											trigger="click" @show="showSelectIcon">
									<template #reference>
										<el-input v-model="entity.icon" placeholder="点击选择图标" @blur="showSelectIcon"
												  v-click-outside="hideSelectIcon" readonly>
											<template #prefix>
												<svg-icon v-if="entity.icon" :icon-class="entity.icon"
														  class="el-input__icon" style="height: 32px;width: 16px;" />
												<el-icon v-else style="height: 32px;width: 16px;">
													<search />
												</el-icon>
											</template>
										</el-input>
									</template>
									<zdx-icon-select ref="iconSelectRef" @selected="selected" :active-icon="entity.icon" />
								</el-popover>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="是否禁用" prop="isEnabled">
								<el-radio-group v-model="entity.isEnabled">
									<el-radio :label="true">是</el-radio>
									<el-radio :label="false">否</el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
			</template>
			<template #footer>
				<el-button type="primary" @click="successHandle(authRef)">
					保存
				</el-button>
			</template>
		</zdxDialog>
	</div>
</template>

<style lang="scss" scoped></style>