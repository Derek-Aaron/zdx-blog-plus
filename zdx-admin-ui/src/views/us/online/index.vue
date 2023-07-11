<script setup >
import zdxPagination from "@/components/Pagination/index.vue";
import { onMounted, reactive, ref } from "vue";
import { page, baseById } from  "@/api/base"

const queryParams = reactive({
    params: {
        username:''
    },
    page: 1,
    limit: 10
})

const module = ref('online')
const queryRef = ref()
const loading = ref(false)
const onlineList = ref([])
const total = ref(0)

const pageOnline = () => {
    loading.value = true;
    page(module.value, queryParams).then(res => {
        onlineList.value = res.data.records
        loading.value = false
        total.value = parseInt(res.data.total)
    })
}

const resetQuery = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}

const handleDelete = (id) => {
    baseById(module.value, 'out', id).then((res) => {
        ElMessage.success(res.message)
        pageOnline()
    })
}

onMounted(() => {
    pageOnline()
})
</script>

<template>
    <div class="app-container">
         <!--用户数据-->
         <el-form :model="queryParams.params" ref="queryRef" :inline="true" label-width="68px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="queryParams.params.username" placeholder="请输入用户名" clearable style="width: 240px"
                    @keyup.enter="pageOnline" @clear="pageOnline" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageOnline">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
            </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="onlineList">
            <el-table-column label="会话编号" align="center" key="personId" prop="personId" show-overflow-tooltip/>
            <el-table-column label="用户名" align="center" key="username" prop="username" show-overflow-tooltip/>
            <el-table-column label="昵称" align="center" key="nickname" prop="nickname" show-overflow-tooltip/>
            <el-table-column label="主机地址" align="center" key="ip" prop="ip" show-overflow-tooltip/>
            <el-table-column label="来源" align="center" key="address" prop="address" show-overflow-tooltip/>
            <el-table-column label="系统" align="center" key="os" prop="os" show-overflow-tooltip/>
            <el-table-column label="浏览器" align="center" key="browser" prop="browser" show-overflow-tooltip/>
            <el-table-column label="登录时间" align="center" key="loginTime" prop="loginTime" show-overflow-tooltip/>
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-tooltip content="强制退出" placement="top">
                        <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.personId)" />
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>

        <zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
            @pagination="pageRole" />

    </div>
</template>

<style scoped>

</style>