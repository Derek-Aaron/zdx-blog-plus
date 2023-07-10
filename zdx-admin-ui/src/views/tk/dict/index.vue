<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, save } from "@/api/base"
import { useDict } from "@/utils/dict" 
import { ElMessage } from "element-plus";

const {zdx_dict_type} = useDict('zdx_dict_type')
const queryParams = reactive({
    params: {
        name: '',
        key: ''
    },
    page: 1,
    limit: 10
})

const columns = reactive([
    { key: 0, label: `编号`, visible: true },
    { key: 1, label: `字典名`, visible: true },
    { key: 2, label: `字典key`, visible: true },
    { key: 3, label: `字典类型`, visible: true },
    { key: 4, label: `是否禁用`, visible: true },
    { key: 5, label: `创建时间`, visible: true },
])

const module = ref('dict')
const queryRef = ref()
const dictList = ref()
const loading = ref(false)
const ids = ref([])
const entity = ref({})
const total = ref(0)

const pageDict = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        dictList.value = res.data.records
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

const handleChange = (row) => {
    save(module.value, row).then(res => {
        ElMessage.success(res.message)
    })
}

onMounted(() => {
    pageDict()
})
</script>
    
<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="角色名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入角色名" clearable style="width: 240px"
                    @keyup.enter="pageDict" @clear="pageDict" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageDict">搜索</el-button>
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
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageDict"
                :columns="columns"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="dictList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
            <el-table-column label="字典名" align="center" key="name" prop="name" v-if="columns[1].visible" />
            <el-table-column label="字典key" align="center" key="key" prop="key" v-if="columns[2].visible" />
            <el-table-column label="字典类型" align="center" key="type" prop="type" v-if="columns[3].visible">
                <template #default="scope">
               <zdx-dict-tag :options="zdx_dict_type" :value="scope.row.type" />
            </template>
            </el-table-column>
            <el-table-column label="是否禁用" align="center" key="isDisabled" v-if="columns[4].visible">
                <template #default="scope">
                    <el-switch v-model="scope.row.isDisabled" class="ml-2" :active-value="false" :inactive-value="true"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        @change="handleChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" key="createTime" prop="createTime" v-if="columns[5].visible" />
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

    </div>
</template>

<style lang="scss" scoped></style>