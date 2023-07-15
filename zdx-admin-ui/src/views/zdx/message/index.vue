<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import { onMounted, reactive, ref } from "vue";
import {page, batchDel, save, batchHandle} from '@/api/base'
import { ElMessage, ElMessageBox } from "element-plus";


const queryParams = reactive({
    params: {
        
    },
    page: 1,
    limit: 10
})

const module = ref('message')
const queryRef = ref()
const showSearch = ref(true)
const loading = ref(false)
const total = ref(0)
const messageList = ref([])
const ids = ref([])
const entity = ref({})

const pageMessage = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        messageList.value = res.data.records
        loading.value = false
        total.value = parseInt(res.data.total)
    })
}

const handleChange = (row) => {
    save(module.value, row).then(res => {
        ElMessage.success(res.message)
        pageMessage()
    })
}

const resetQuery = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}

const handleThrough = () => {
    batchHandle(module.value, 'through', ids.value).then(res => {
        ElMessage.success(res.message)
        pageMessage()
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
         pageMessage()
      })
   })
}

const handleSelectionChange = (selection) => {
    ids.value = selection.map(row => row.id)
    if (selection.length === 1) {
        entity.value = selection[0]
    }
}


onMounted(() => {
    pageMessage()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="用户昵称" prop="name">
                <el-input v-model="queryParams.params.nickname" placeholder="请输入用户昵称" clearable style="width: 240px"
                    @keyup.enter="pageMessage" @clear="pageMessage" />
            </el-form-item>
            <el-form-item label="审核" prop="isCheck">
                <el-radio-group v-model="queryParams.params.isCheck">
                    <el-radio :label="true">通过</el-radio>
                    <el-radio :label="false">不通过</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageMessage">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="success" plain icon="Edit" :disabled="!ids.length > 0" @click="handleThrough">批量通过</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
                    @click="handleDelete">删除</el-button>
            </el-col>
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageMessage"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip />
            <el-table-column label="昵称" align="center" key="nickname" prop="nickname" show-overflow-tooltip />
            <el-table-column label="留言内容" align="center" key="content" prop="content" show-overflow-tooltip/>
            <el-table-column label="地址" align="center" key="ip" prop="ip" show-overflow-tooltip/>
            <el-table-column label="来源" align="center" key="source" prop="source" show-overflow-tooltip/>
            <el-table-column label="审核" align="center" key="isCheck" prop="isCheck" show-overflow-tooltip >
								<template #default="scope">
										<el-switch v-model="scope.row.isCheck" class="ml-2" :active-value="false" :inactive-value="true"
															 style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
															 @change="handleChange(scope.row)"></el-switch>
								</template>
            </el-table-column>
            <el-table-column label="留言时间" align="center" key="createTime" prop="createTime" />
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-tooltip content="删除" placement="top">
                        <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)" />
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>

        <zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
            @pagination="pageMessage" />
    </div>
</template>

<style lang="scss" scoped></style>