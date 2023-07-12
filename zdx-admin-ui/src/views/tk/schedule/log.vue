<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import { onMounted, reactive, ref } from "vue";
import { page, batchDel, baseById } from '@/api/base'
import { useRoute } from "vue-router";
import router from "@/router";
import { ElMessage, ElMessageBox } from "element-plus";


console.log(useRoute().params);

const queryParams = reactive({
    params: {
        scheduleId: useRoute().params.scheduleId
    },
    page: 1,
    limit: 10
})

const module = ref('schedule-log')
const queryRef = ref()
const showSearch = ref(true)
const logList = ref([])
const total = ref(0)
const loading = ref(false)
const ids = ref([])
const entity = ref({})

const pageLog = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        logList.value = res.data.records
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

const resetQuery = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}

const handleClear = () => {
    baseById(module.value, 'clear', queryParams.params.scheduleId).then(res => {
        ElMessage.success(res.message);
        pageLog()
    })
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
            pageLog()
        })
    })
}

onMounted(() => {
    pageLog()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="任务名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入任务名" clearable style="width: 240px"
                    @keyup.enter="pageRole" @clear="pageRole" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageLog">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
                    @click="handleDelete">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" @click="handleClear">清空</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="Close" @click="router.push('/schedule')">返回</el-button>
            </el-col>
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageLog"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip />
            <el-table-column label="任务名" align="center" key="name" prop="name" />
            <el-table-column label="开始时间" align="center" key="startTime" prop="startTime" />
            <el-table-column label="结束时间" align="center" key="oldTime" prop="oldTime" />
            <el-table-column label="状态" align="center" key="status" prop="status">
                <template #default="scope">
                    {{ scope.row.status ? '是' : '否' }}
                </template>
            </el-table-column>
            <el-table-column label="异常信息" align="center" key="exceptionInfo" prop="exceptionInfo" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-tooltip content="删除" placement="top">
                        <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)" />
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>

        <zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
            @pagination="pageLog" />

    </div>
</template>

<style lang="scss" scoped></style>