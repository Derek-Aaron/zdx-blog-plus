<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDictTag from "@/components/DictTag/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, batchDel, baseParams } from '@/api/base'
import { useRoute } from "vue-router";
import { useDict } from "@/utils/dict";
import { ElMessage, ElMessageBox } from "element-plus";

const { zdx_log_event } = useDict('zdx_log_event')


const queryParams = reactive({
    params: {
        event: useRoute().meta.params.event
    },
    page: 1,
    limit: 10,
})

const module = ref('log')
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
    baseParams(module.value, 'clear', { event: queryParams.params.event }).then(res => {
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
            <el-form-item label="用户名" prop="name">
                <el-input v-model="queryParams.params.username" placeholder="请输入用户名" clearable style="width: 240px"
                    @keyup.enter="pageRole" @clear="pageRole" />
            </el-form-item>
            <el-form-item label="用户名" prop="name">
                <el-select v-model="queryParams.params.event" placeholder="请选择" clearable @clear="pageLog"
                    @keyup.enter="pageLog">
                    <el-option v-for="item in zdx_log_event" :key="item.value" :label="item.value" :value="item.key" />

                </el-select>
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
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageLog"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip />
            <el-table-column label="事件" align="center" key="event" prop="event" show-overflow-tooltip>
                <template #default="scope">
                    <zdx-dict-tag :options="zdx_log_event" :value="scope.row.event" />
                </template>
            </el-table-column>
            <el-table-column label="用户名" align="center" key="username" prop="username" show-overflow-tooltip />
            <el-table-column label="访问链接" align="center" key="url" prop="url" show-overflow-tooltip />
            <el-table-column label="访问地址" align="center" key="ip" prop="ip" show-overflow-tooltip />
            <el-table-column label="来源" align="center" key="source" prop="source" show-overflow-tooltip />
            <el-table-column label="系统" align="center" key="os" prop="os" show-overflow-tooltip />
            <el-table-column label="浏览器" align="center" key="browser" prop="browser" show-overflow-tooltip />
            <el-table-column label="访问时间" align="center" key="createTime" prop="createTime" show-overflow-tooltip />
            <el-table-column label="内容" align="center" key="content" prop="content" show-overflow-tooltip />
            <el-table-column label="状态" align="center" key="status" prop="status">
                <template #default="scope">
                    {{ scope.row.status ? '是' : '否' }}
                </template>
            </el-table-column>
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