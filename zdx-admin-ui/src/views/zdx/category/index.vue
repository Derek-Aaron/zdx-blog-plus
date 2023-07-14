<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, batchDel, save } from '@/api/base'
import { ElMessage, ElMessageBox } from "element-plus";

const queryParams = reactive({
    params: {
        name: ''
    },
    page: 1,
    limit: 10
})

const rules = reactive({
    name: [{ required: true, message: "分类名不能为空", trigger: "blur" }, { min: 2, max: 20, message: "分类名长度必须介于 2 和 20 之间", trigger: "blur" }],
})

const module = ref('category')
const queryRef = ref()
const showSearch = ref(true)
const loading = ref(false)
const total = ref(0)
const categoryList = ref([])
const ids = ref([])
const entity = ref({})
const dialog = ref(false)
const title = ref('')
const formRef = ref()

const pageCategory = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        categoryList.value = res.data.records
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
    title.value = '新增分类'
    entity.value = {}
}

const handleUpdate = (row) => {
    if (!(row instanceof PointerEvent)) {
        if (row) {
            entity.value = row
        }
    }
    dialog.value = true
    title.value = '编辑分类 【' + entity.value.name + '】'
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
         pageCategory()
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
                    pageCategory()
                })
            }
        } else {
            console.log('error submit!', fields)
        }
    })
}

onMounted(() => {
    pageCategory()
})

</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="分类名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入分类名" clearable style="width: 240px"
                    @keyup.enter="pageCategory" @clear="pageCategory" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageCategory">搜索</el-button>
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
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageCategory"
               ></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip />
            <el-table-column label="分类名" align="center" key="name" prop="name" />
            <el-table-column label="创建时间" align="center" key="createTime" prop="createTime"/>
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
            @pagination="pageCategory" />

        <zdx-dialog :dialog="dialog" :title="title" @close="dialog = false">
            <template #content>
                <el-form :model="entity" :rules="rules" ref="formRef" label-width="80px">
                    <el-form-item label="分类名" prop="name">
                        <el-input v-model="entity.name" clearable placeholder="请输入分类名" />
                    </el-form-item>
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