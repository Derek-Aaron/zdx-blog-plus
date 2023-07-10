<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, batchDel } from '@/api/base'
import { ElMessage } from "element-plus";

const module = ref('role')
const roleRef = ref()
const roleList = ref([])
const loading = ref(false)
const dialog = ref(false)
const title = ref('')
const total = ref(0)
const showSearch = ref(true)
const queryRef = ref()
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
    name: [{ required: true, message: "角色不能为空", trigger: "blur" }, { min: 2, max: 20, message: "角色长度必须介于 2 和 20 之间", trigger: "blur" }],
    display: [{ required: true, message: "角色名不能为空", trigger: "blur" }],
})


const pageRole = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        roleList.value = res.data.records
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
    if (row) {
        entity.value = row
    }
    dialog.value = true
    title.value = '编辑角色 【' + entity.value.name + '】'
}

const handleDelete = (id) => {
    if (id) {
        ids.value = [id]
    }
    batchDel(module.value, ids.value).then(res => {
        ElMessage.success(res.message)
        pageRole()
    })
}

const successHandle = () => {
    if (!formEl) return
    formEl.validate((valid, fields) => {
        if (valid) {
            if (Object.keys(entity.value.length !== 0)) {
                save(module.value, entity.value).then(res => {
                    ElMessage.success(res.message)
                    dialog.value = false
                    pageRole()
                })
            }
        } else {
            console.log('error submit!', fields)
        }
    })
}

onMounted(() => {
    pageRole()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="角色名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入角色名" clearable style="width: 240px"
                    @keyup.enter="pageRole" @clear="pageRole" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageRole">搜索</el-button>
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
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageRole"
                :columns="columns"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
            <el-table-column label="角色" align="center" key="name" prop="name" v-if="columns[1].visible" />
            <el-table-column label="角色名" align="center" key="display" prop="display" v-if="columns[2].visible" />
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
            @pagination="pageUser" />
        <zdxDialog :dialog="dialog" :title="title" @close="dialog = false">
            <template #content>
                <el-form :model="entity" :rules="rules" ref="roleRef" label-width="80px">
                    <el-form-item label="角色" prop="name">
                        <el-input v-model="entity.name" placeholder="请输入角色" maxlength="30" clearable />
                    </el-form-item>
                    <el-form-item label="角色名" prop="display">
                        <el-input v-model="entity.display" placeholder="请输入角色名" maxlength="30" clearable />
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="entity.description" type="textarea" placeholder="请输入内容"></el-input>
                    </el-form-item>
                </el-form>
            </template>
            <template #footer>
                <el-button type="primary" @click="successHandle(roleRef)">
                    保存
                </el-button>
            </template>
        </zdxDialog>
    </div>
</template>

<style lang="scss" scoped></style>