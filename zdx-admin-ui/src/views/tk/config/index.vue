<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, save, batchDel } from "@/api/base"
import { useDict } from "@/utils/dict";
import { ElMessage, ElMessageBox } from "element-plus";

const { zdx_config_type } = useDict('zdx_config_type')
const queryParams = reactive({
    params: {
        name: '',
        type: ''
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
    name: [{ required: true, message: "配置名不能为空", trigger: "blur" }, { min: 2, max: 20, message: "配置名长度必须介于 2 和 20 之间", trigger: "blur" }],
    type: [{ required: true, message: "配置类型不能为空", trigger: "blur" }],
    value: [{ required: true, message: "配置值不能为空", trigger: "blur" }],
})

const module = ref('config')
const queryRef = ref()
const configRef = ref()
const showSearch = ref(true)
const configList = ref([])
const loading = ref(false)
const dialog = ref(false)
const title = ref('')
const total = ref(0)
const ids = ref([])
const entity = ref({})



const pageConfig = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        configList.value = res.data.records
        loading.value = false
        total.value = parseInt(res.data.total)
    })
}

const handleSelectionChange = (selection) => {
    ids.value = selection.map(row => row.id)
    if (selection.length === 1) {
        entity.value = selection[0]
    }
}

const handleUpdate = (row) => {
    if (row) {
        entity.value = row
    }
    dialog.value = true
    title.value = '更改配置【' + entity.value.name + '】'
}

const handleAdd = () => {
    dialog.value = true
    title.value = '创建配置'
    entity.value = {}
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
           pageConfig()
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
                    pageConfig()
                })
            }
        } else {
            console.log('error submit!', fields)
        }
    })
}

onMounted(() => {
    pageConfig()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="配置名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入配置名" clearable style="width: 240px"
                    @keyup.enter="pageConfig" @clear="pageConfig" />
            </el-form-item>
            <el-form-item label="配置类型" prop="type">
                <el-select v-model="queryParams.params.type" class="m-2" placeholder="请选择" clearable>
                    <el-option v-for="item in zdx_config_type" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageConfig">搜索</el-button>
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
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageConfig"
                :columns="columns"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
            <el-table-column label="配置名" align="center" key="name" prop="name" show-overflow-tooltip=""
                v-if="columns[1].visible" />
            <el-table-column label="配置类型" align="center" key="type" prop="type" v-if="columns[2].visible">
                <template #default="scope">
                    <zdx-dict-tag :options="zdx_config_type" :value="scope.row.type" />
                </template>
            </el-table-column>
            <el-table-column label="描述" align="center" key="description" prop="description" show-overflow-tooltip=""
                v-if="columns[1].visible" />
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
            @pagination="pageConfig" />

        <zdx-dialog :dialog="dialog" :title="title" @close="dialog = false">
            <template #content>
                <el-form :model="entity" :rules="rules" ref="configRef" label-width="80px">
                    <el-form-item label="配置名" prop="name">
                        <el-input v-model="entity.name" placeholder="请输入配置名" clearable />
                    </el-form-item>
                    <el-form-item label="配置类型" prop="type">
                        <el-select v-model="entity.type" class="m-2" placeholder="请选择" clearable>
                            <el-option v-for="item in zdx_config_type" :key="item.key" :label="item.value"
                                :value="item.key" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="配置值" prop="value">
                        <el-input v-model="entity.value" placeholder="请输入配置值" clearable />
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="entity.description" type="textarea" placeholder="请输入内容" clearable />
                    </el-form-item>
                </el-form>
            </template>
            <template #footer>
                <el-button type="primary" @click="successHandle(configRef)">
                    保存
                </el-button>
            </template>
        </zdx-dialog>
    </div>
</template>

<style lang="scss" scoped></style>