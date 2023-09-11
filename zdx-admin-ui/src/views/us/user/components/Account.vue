<script setup>
import zdxPagination from "@/components/Pagination/index.vue";
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import {onMounted, reactive, ref, watch} from "vue";
import {batchDel, page, save, saveEncrypt} from "@/api/base";
import {ElMessage, ElMessageBox} from "element-plus";
const props = defineProps({
	dialog:{
		type: Boolean,
		default: false,
		required: true
	},
	entity:{
		type: Object,
		default: {},
	}
})

const emits = defineEmits(['close'])

const module = ref('account')
const accountList = ref([])
const loading = ref(false)
const total = ref(0)
const ids = ref([])
const entity = ref({})
const showSearch = ref(true)
const saveDialog = ref(false)
const title = ref('')
const accountRef = ref()

const queryParams = reactive({
	params: {
		userId: props.entity.id
	},
	page: 1,
	limit: 10
})

const columns = reactive([
	{ key: 0, label: `账号编号`, visible: true },
	{ key: 1, label: `账号`, visible: true },
	{ key: 2, label: `是否禁用`, visible: true },
	{ key: 3, label: `是否锁定`, visible: true },
])

const rules = reactive({

})

watch(() => props.dialog, (value) => {
	if (value ) {
		pageAccount()
	}
}, {deep: true})

const pageAccount = () => {
	loading.value = true
	queryParams.params.userId = props.entity.id
	page(module.value, queryParams).then((res) => {
		accountList.value = res.data.records
		loading.value = false
		total.value = parseInt(res.data.total)
	})
}

const handleSelectionChange = (selection) => {
	ids.value = selection.map(item => item.id);
	if (selection.length === 1) {
		entity.value = selection[0]
	}
}

const handleClose = (done) => {
  	done()
	emits('close')
}

const handleChange = (row) => {
  save(module.value, row).then((res) => {
	  ElMessage.success(res.message)
	  pageAccount()
  })
}
const handleAdd = () => {
  	saveDialog.value = true
	title.value = '新增账号'
}

const handleUpdate = (row) => {
	if (!(row instanceof PointerEvent)) {
		if (row) {
			entity.value = row
		}
	}
	if (Object.keys(entity.value).length === 0) {
		ElMessage.error('请选择实例')
		return
	}
	saveDialog.value = true
	title.value = '编辑账号【' + entity.value.username + '】'
}

const handleDelete = () => {
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
			pageAccount()
		})
	})
}

const successHandle = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				entity.value.userId = props.entity.id
				saveEncrypt(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					dialog.value = false
					pageAccount()
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}


onMounted(() => {
	pageAccount()
})
</script>

<template>
	<div class="app-container">
		<el-dialog :model-value="$props.dialog" title="账号管理" :before-close="handleClose"
				   draggable :fullscreen="true">
			<el-row :gutter="10" class="mb8">
				<el-col :span="1.5">
					<el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
				</el-col>
				<el-col :span="1.5">
					<el-button type="success" plain icon="Edit" :disabled="!ids.length > 0" @click="handleUpdate">修改</el-button>
				</el-col>
				<el-col :span="1.5">
					<el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0" @click="handleDelete">删除</el-button>
				</el-col>
				<zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageAccount" :columns="columns"></zdx-right-toolbar>
			</el-row>
			<el-table v-loading="loading" :data="accountList" @selection-change="handleSelectionChange">
				<el-table-column type="selection" width="50" align="center" />
				<el-table-column label="账号编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
				<el-table-column label="账号" align="center" key="username" prop="username" v-if="columns[1].visible"
								 show-overflow-tooltip />
				<el-table-column label="是否禁用" align="center" key="isDisabled" v-if="columns[2].visible">
					<template #default="scope">
						<el-switch v-model="scope.row.isDisabled" class="ml-2" :active-value="false" :inactive-value="true"
								   style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
								   @change="handleChange(scope.row)"></el-switch>
					</template>
				</el-table-column>
				<el-table-column label="是否锁定" align="center" key="isLocked" v-if="columns[3].visible">
					<template #default="scope">
						<el-switch v-model="scope.row.isLocked" class="ml-2" :active-value="false" :inactive-value="true"
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
<!--						<el-dropdown size="small" hide-on-click>-->
<!--							<el-button text size="small" type="primary" icon="DArrowRight" plain>更多</el-button>-->
<!--							<template #dropdown>-->
<!--								<el-dropdown-item>-->
<!--									<el-tooltip content="重置密码" placement="top">-->
<!--										<el-button link type="primary" icon="Key" @click="handleResetPwd(scope.row.id)" />-->
<!--									</el-tooltip>-->
<!--								</el-dropdown-item>-->
<!--								<el-dropdown-item>-->
<!--									<el-tooltip content="分配角色" placement="top">-->
<!--										<el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row.id)" />-->
<!--									</el-tooltip>-->
<!--								</el-dropdown-item>-->
<!--							</template>-->
<!--						</el-dropdown>-->
					</template>
				</el-table-column>
			</el-table>
			<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
							@pagination="pageAccount" />
			<zdx-dialog :title="title" :dialog="saveDialog" @close="saveDialog = false">
				<template #content>
					<el-form :model="entity" :rules="rules" ref="accountRef" label-width="80px">
						<el-form-item prop="username" label="账号">
							<el-input v-model="entity.username" placeholder="请输入账号名" clearable/>
						</el-form-item>
						<el-form-item prop="password" label="密码">
							<el-input type="password" v-model="entity.password" placeholder="请输入密码" clearable/>
						</el-form-item>
						<el-row>
							<el-col :span="12">
								<el-form-item label="是否禁用">
									<el-radio-group v-model="entity.isDisabled">
										<el-radio :label="true">是</el-radio>
										<el-radio :label="false">否</el-radio>
									</el-radio-group>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="是否锁定">
									<el-radio-group v-model="entity.isLocked">
										<el-radio :label="true">是</el-radio>
										<el-radio :label="false">否</el-radio>
									</el-radio-group>
								</el-form-item>
							</el-col>
						</el-row>
					</el-form>
				</template>
				<template #footer>
					<el-button type="primary" @click="successHandle(accountRef)">
						保存
					</el-button>
				</template>
			</zdx-dialog>
		</el-dialog>
	</div>

</template>

<style scoped lang="scss">

</style>