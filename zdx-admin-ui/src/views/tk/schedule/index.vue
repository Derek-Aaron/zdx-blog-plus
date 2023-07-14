<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDictTag from "@/components/DictTag/index.vue"
import zdxDialog from "@/components/Dialog/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, save, batchDel, baseById } from "@/api/base"
import { useDict } from "@/utils/dict";
import router from "@/router";

import { ElMessage, ElMessageBox } from "element-plus";

const { zdx_schedule_misfire } = useDict('zdx_schedule_misfire')

const queryParams = reactive({
    params: {

    },
    page: 1,
    limit: 10
})

const columns = reactive([
    { key: 0, label: `编号`, visible: true },
    { key: 1, label: `任务名`, visible: true },
    { key: 2, label: `任务分组`, visible: true },
    { key: 3, label: `任务扩展`, visible: true },
    { key: 4, label: `表达式`, visible: true },
    { key: 5, label: `状态`, visible: true },

])

const rules = reactive({
    name: [{ required: true, message: "任务名不能为空", trigger: "blur" }],
    group: [{ required: true, message: "任务分组不能为空", trigger: "blur" }],
    invoke: [{ required: true, message: "执行扩展不能为空", trigger: "blur" }],
    cron: [{ required: true, message: "表达式不能为空", trigger: "blur" }]
})


const module = ref('schedule')
const queryRef = ref()
const showSearch = ref(true)
const loading = ref(false)
const dialog = ref(false)
const openView = ref(false)
const formRef = ref()
const title = ref('')
const scheduleList = ref([])
const ids = ref([])
const entity = ref({})
const total = ref(0)

const pageSchedule = () => {
    loading.value = true
    page(module.value, queryParams).then(res => {
        scheduleList.value = res.data.records
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
            pageSchedule()
        })
    })
}

const handleAdd = () => {
    dialog.value = true
    title.value = '创建任务'
    entity.value = {}
}

const handleUpdate = (row) => {
    if (row) {
        entity.value = row
    }
    title.value = '更改任务【' + entity.value.name + '】'
    dialog.value = true
}
const handleChange = (row) => {
    save(module.value, row).then((res) => {
        ElMessage.success(res.message)
        pageSchedule()
    })
}

const handleRun = (id) => {
    baseById(module.value, 'run', id).then((res) => {
        ElMessage.success(res.message)
    })
}

const handleView = (row) => {
    entity.value = row
    openView.value = true
}

const handleLog = (id) => {
    if (typeof id == "string" && id) {
        ids.value = [id]
    }
    router.push(`/schedule-log/${ids.value[0]}`)
}

const successHandle = (formEl) => {
    if (!formEl) return
    formEl.validate((valid, fields) => {
        if (valid) {
            if (Object.keys(entity.value.length !== 0)) {
                save(module.value, entity.value).then(res => {
                    ElMessage.success(res.message)
                    dialog.value = false
                    pageSchedule()
                })
            }
        } else {
            console.log('error submit!', fields)
        }
    })
}

onMounted(() => {
    pageSchedule()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="任务名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入任务名" clearable style="width: 240px"
                    @keyup.enter="pageSchedule" @clear="pageSchedule" />
            </el-form-item>
            <el-form-item label="分组" prop="group">
                <el-input v-model="queryParams.params.group" placeholder="请输入分组" clearable style="width: 240px"
                    @keyup.enter="pageSchedule" @clear="pageSchedule" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-radio-group v-model="queryParams.params.status" @keyup.enter="pageSchedule">
                    <el-radio :label="true">开启</el-radio>
                    <el-radio :label="false">暂停</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="pageSchedule">搜索</el-button>
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
            <el-col :span="1.5">
                <el-button type="info" plain icon="List" :disabled="!ids.length > 0" @click="handleLog">日志</el-button>
            </el-col>
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageSchedule"
                :columns="columns"></zdx-right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="scheduleList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
            <el-table-column label="任务名" align="center" key="name" prop="name" show-overflow-tooltip
                v-if="columns[1].visible" />
            <el-table-column label="任务分组" align="center" key="group" prop="group" show-overflow-tooltip
                v-if="columns[2].visible" />
            <el-table-column label="任务扩展" align="center" key="invoke" prop="invoke" show-overflow-tooltip
                v-if="columns[3].visible" />
            <el-table-column label="表达式" align="center" key="cron" prop="cron" show-overflow-tooltip
                v-if="columns[4].visible" />
            <el-table-column label="状态" align="center" key="status" prop="status" show-overflow-tooltip
                v-if="columns[5].visible">
                <template #default="scope">
                    <el-switch v-model="scope.row.status" class="ml-2" :active-value="true" :inactive-value="false"
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
                    <el-dropdown size="small" hide-on-click>
                        <el-button text size="small" type="primary" icon="DArrowRight" plain>更多</el-button>
                        <template #dropdown>
                            <el-dropdown-item>
                                <el-tooltip content="执行一次" placement="top">
                                    <el-button link type="primary" icon="CaretLeft" @click="handleRun(scope.row.id)" />
                                </el-tooltip>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <el-tooltip content="任务详细" placement="top">
                                    <el-button link type="primary" icon="View" @click="handleView(scope.row)" />
                                </el-tooltip>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <el-tooltip content="任务日志" placement="top">
                                    <el-button link type="primary" icon="List" @click="handleLog(scope.row.id)" />
                                </el-tooltip>
                            </el-dropdown-item>
                        </template>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>

        <zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
            @pagination="pageSchedule" />

        <zdx-dialog :dialog="dialog" :title="title" @close="dialog = false">
            <template #content>
                <el-form :model="entity" :rules="rules" ref="formRef" label-width="120px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="任务名">
                                <el-input v-model="entity.name" placeholder="请输入任务名" clearable />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="任务分组">
                                <el-select v-model="entity.group" placeholder="请选择分组">
                                    <el-option label="默认" value="默认" />
                                    <el-option label="系统" value="系统" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="扩展类" prop="invoke">
                                <template #label>
                                    <span>
                                        调用类
                                        <el-tooltip placement="top">
                                            <template #content>
                                                <div>
                                                    Bean调用示例：bean:com.zdx.XXX
                                                    <br />Class类调用示例：class: com.zdx.XXX
                                                </div>
                                            </template>
                                            <el-icon><question-filled /></el-icon>
                                        </el-tooltip>
                                    </span>
                                </template>
                                <el-input v-model="entity.invoke" placeholder="请输入执行类" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="表达式" prop="cron">
                                <el-input v-model="entity.cron" placeholder="请输入cron执行表达式">
                                    <!-- <template #append>
                                        <el-button type="primary" @click="handleShowCron">
                                            生成表达式
                                            <i class="el-icon-time el-icon--right"></i>
                                        </el-button>
                                    </template> -->
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="执行策略" prop="misfire">
                                <el-select v-model="entity.misfire" placeholder="请选择执行策略">
                                    <el-option v-for="item in zdx_schedule_misfire" :label="item.value" :value="item.key"
                                        :key="item.key" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="状态" prop="status">
                                <el-radio-group v-model="entity.status">
                                    <el-radio :label="true">开启</el-radio>
                                    <el-radio :label="false">暂停</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="并发执行" prop="concurrent">
                                <el-radio-group v-model="entity.concurrent">
                                    <el-radio :label="true">是</el-radio>
                                    <el-radio :label="false">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="备注">
                                <el-input v-model="entity.description" type="textarea" placeholder="请输入内容"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </template>
            <template #footer>
                <el-button type="primary" @click="successHandle(formRef)">
                    保存
                </el-button>
            </template>
        </zdx-dialog>
        <zdx-dialog :dialog="openView" title="查询任务详情" @close="openView = false">
            <template #content>
                <el-descriptions column="2">
                    <el-descriptions-item label="任务编号：">{{ entity.id }}</el-descriptions-item>
                    <el-descriptions-item label="任务名：">{{ entity.name }}</el-descriptions-item>
                    <el-descriptions-item label="任务分组：">{{ entity.group }}</el-descriptions-item>
                    <el-descriptions-item label="扩展类：">{{ entity.invoke }}</el-descriptions-item>
                    <el-descriptions-item label="表达式：">{{ entity.cron }}</el-descriptions-item>
                    <el-descriptions-item label="执行策略："> <zdx-dict-tag :options="zdx_schedule_misfire"
                            :value="entity.misfire" /></el-descriptions-item>
                    <el-descriptions-item label="状态：">{{ entity.status ? '开启' : '暂停' }}</el-descriptions-item>
                    <el-descriptions-item label="是否并发：">{{ entity.concurrent ? '是' : '否' }}</el-descriptions-item>
                    <el-descriptions-item label="创建时间：">{{ entity.createTime }}</el-descriptions-item>
                    <el-descriptions-item label="描述：">{{ entity.description }}</el-descriptions-item>
                </el-descriptions>
            </template>
        </zdx-dialog>
    </div>
</template>

<style scoped></style>