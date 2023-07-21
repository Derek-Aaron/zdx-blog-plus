<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxDictTag from "@/components/DictTag/index.vue"
import zdxIconSelect from "@/components/IconSelect/index.vue"
import zdxRoleAuth from './components/roleAuth.vue'
import zdxUserAuth from './components/userAuth.vue'
import { onMounted, reactive, ref, nextTick } from "vue";
import { useDict } from "@/utils/dict";
import { tree, updateMenuStatic } from "@/api/tk/menu"
import { ElMessage, ElMessageBox } from "element-plus";
import { save, batchDel } from "@/api/base"
import { subject } from "@/api/us/acl"


const { zdx_dict_menu_type } = useDict('zdx_dict_menu_type')
const queryRef = ref();
const module = ref('menu')
const showSearch = ref(true)
const showChooseIcon = ref(false)
const isExpandAll = ref(false)
const dialog = ref(false)
const title = ref('')
const refreshTable = ref(true)
const loading = ref(false)
const openAuth = ref(false)
const menuList = ref()
const iconSelectRef = ref()
const menuRef = ref()
const activeTab = ref('role')
const ids = ref([])
const entity = ref({})
const entityId = ref()
const roleIds = ref([])
const userIds = ref([])

const queryParams = reactive({
    params: {

    },
    page: 1,
    limit: 10
})

const rules = reactive({
    name: [{ required: true, message: "菜单名不能为空", trigger: "blur" }],
    type: [{ required: true, message: "请选择类型", trigger: "blur" }],
    params: [{ required: true, message: "请输入权限标识", trigger: "blur" }],
})


const resetQuery = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
}

const toggleExpandAll = () => {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
        refreshTable.value = true;
    });
}
const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.id);
    if (selection.length === 1) {
        entity.value = selection[0]
    }
}
const listMenu = () => {
    loading.value = true;
    tree(queryParams).then(res => {
        loading.value = false;
        menuList.value = res.data
    })
}

const handleChange = (row) => {
    updateMenuStatic({
        menuId: row.id,
        isDisabled: row.isDisabled
    }).then(res => {
        ElMessage.success(res.message)
        listMenu()
    })
}

const close = () => {
    dialog.value = false;
    showChooseIcon.value = false
}

const showSelectIcon = () => {
    iconSelectRef.value.reset();
    showChooseIcon.value = true;
}

const selected = (name) => {
    entity.value.icon = name;
    showChooseIcon.value = false;
}

const handleAdd = (row) => {
    entity.value = {}
    if (row) {
        entity.value.parentId = row.id
    }
    title.value = '新增菜单'
    dialog.value = true
}

const handleUpdate = (row) => {
    if (!(row instanceof PointerEvent)) {
        if (row) {
            entity.value = row
        }
    }
    dialog.value = true
    title.value = '更改菜单【' + entity.value.name + '】'
    entity.value = row
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
            listMenu()
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
                    listMenu()
                })
            }
        } else {
            console.log('error submit!', fields)
        }
    })
}

const roleChange = (data) => {
    data.resources = ids.value
    save('acl', data).then(res => {
        ElMessage.success(res.message)
    })
}

const userChange = (data) => {
    data.resources = ids.value
    save('acl', data).then(res => {
        ElMessage.success(res.message)
    })
}

const handleAuth = (id) => {
	let array = []
	let flag = false
	if (typeof id === 'string' && id) {
		flag = true
		ids.value = []
		array.push(id)
	}
	if (ids.value.length !== 0) {
		array = ids.value
	}
	array = array.map(item => 'tk:menu:' + item)
	ids.value = array
	openAuth.value = true
	if (flag) {
		subject({ resource: 'tk:menu:' + id }).then(res => {
			let roleArray = []
			let userArray = []
			for (const item of res.data) {
				if (item.subject && item.subject.indexOf('us:role:') !== -1) {
					roleArray.push(item.subject)
				}
				if (item.subject && item.subject.indexOf('us:user:') !== -1) {
					userArray.push(item.subject)
				}
			}
			roleIds.value = roleArray
			userIds.value = userArray
			entityId.value = id
		})
	}
}
onMounted(() => {
    listMenu()
})
</script>

<template>
    <div class="app-container">
        <!--用户数据-->
        <el-form :model="queryParams.params" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="菜单名" prop="name">
                <el-input v-model="queryParams.params.name" placeholder="请输入角色名" clearable style="width: 240px"
                    @keyup.enter="listMenu" @clear="listMenu" />
            </el-form-item>
            <el-form-item label="是否禁用" prop="isDisabled">
                <el-radio-group v-model="queryParams.params.isDisabled" @keyup.enter="listMenu">
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="listMenu">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery(queryRef)">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
            </el-col>
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Plus" @click="handleAuth">批量权限</el-button>
			</el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0"
                    @click="handleDelete">批量删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
            </el-col>
            <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="listMenu"></zdx-right-toolbar>
        </el-row>

        <el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="id" :default-expand-all="isExpandAll"
            :tree-props="{ children: 'children' }" @selection-change="handleSelectionChange">

            <el-table-column type="selection" width="50" align="center" />
            <el-table-column prop="name" label="菜单名称" show-overflow-tooltip />
            <el-table-column prop="icon" label="图标" align="center">
                <template #default="scope">
                    <svg-icon :icon-class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="order" label="排序" />
            <el-table-column prop="action" label="动作" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" show-overflow-tooltip>
                <template #default="scope">
                    <zdx-dict-tag :options="zdx_dict_menu_type" :value="scope.row.type" />
                </template>
            </el-table-column>
            <el-table-column prop="params" label="权限标识" show-overflow-tooltip />
            <el-table-column label="是否禁用" align="center" key="isDisabled">
                <template #default="scope">
                    <el-switch v-model="scope.row.isDisabled" class="ml-2" :active-value="false" :inactive-value="true"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        @change="handleChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="210" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-tooltip content="更改菜单" placement="top">
                        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" />
                    </el-tooltip>
                    <el-tooltip content="添加子菜单" placement="top">
                        <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" />
                    </el-tooltip>
                    <el-dropdown size="small" hide-on-click>
                        <el-button text size="small" type="primary" icon="DArrowRight" plain>更多</el-button>
                        <template #dropdown>
                            <el-dropdown-item>
                                <el-tooltip content="分配角色" placement="top">
                                    <el-button link type="primary" icon="CircleCheck" @click="handleAuth(scope.row.id)" />
                                </el-tooltip>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <el-tooltip content="删除" placement="top">
                                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row.id)" />
                                </el-tooltip>
                            </el-dropdown-item>
                        </template>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>

        <zdx-dialog :dialog="dialog" :title="title" @close="close" width="50%">
            <template #content>
                <el-form :model="entity" :rules="rules" ref="menuRef" label-width="80px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="菜单名称" prop="name">
                                <el-input v-model="entity.name" placeholder="请输入菜单名" clearable />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="类型" prop="type">
                                <el-radio-group v-model="entity.type">
                                    <el-radio v-for="item in zdx_dict_menu_type" :label="item.key" :key="item.key"> {{
                                        item.value }}</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="权限标识" prop="params">
                                <el-input v-model="entity.params" placeholder="请输入权限标识" clearable />
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="是否禁用" prop="isDisabled">
                                <el-radio-group v-model="entity.isDisabled">
                                    <el-radio :label="true">是</el-radio>
                                    <el-radio :label="false">否</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="上级菜单">
                                <el-tree-select v-model="entity.parentId" :data="menuList" style="width: 100%;"
                                    :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id"
                                    placeholder="选择上级菜单" check-strictly clearable />
                            </el-form-item>
                        </el-col>
                        <el-col :span="24" v-if="entity.type !== 'BUTTON'">
                            <el-form-item label="菜单图标" prop="icon">
                                <el-popover placement="bottom-start" :width="540" v-model:visible="showChooseIcon"
                                    trigger="click" @show="showSelectIcon">
                                    <template #reference>
                                        <el-input v-model="entity.icon" placeholder="点击选择图标" @blur="showSelectIcon"
                                            v-click-outside="hideSelectIcon" readonly>
                                            <template #prefix>
                                                <svg-icon v-if="entity.icon" :icon-class="entity.icon"
                                                    class="el-input__icon" style="height: 32px;width: 16px;" />
                                                <el-icon v-else style="height: 32px;width: 16px;">
                                                    <search />
                                                </el-icon>
                                            </template>
                                        </el-input>
                                    </template>
                                    <zdx-icon-select ref="iconSelectRef" @selected="selected" :active-icon="entity.icon" />
                                </el-popover>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12" v-if="entity.type === 'MENU'">
                            <el-form-item label="动作" prop="action">
                                <el-input v-model="entity.action" placeholder="请输入动作" clearable />
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
                <el-button type="primary" @click="successHandle(menuRef)">
                    保存
                </el-button>
            </template>
        </zdx-dialog>
        <zdx-dialog :dialog="openAuth" @close="openAuth = false" title="权限" width="50%">
            <template #content>
                <el-card>
                    <template #header>
                        <div class="clearfix">
                            <span>权限设置</span>
                        </div>
                    </template>
                    <el-tabs v-model="activeTab">
                        <el-tab-pane label="角色" name="role">
                            <zdx-role-auth v-model="roleIds" @change="roleChange" />
                        </el-tab-pane>
                        <el-tab-pane label="用户" name="user">
                            <zdx-user-auth v-model="userIds" @change="userChange" />
                        </el-tab-pane>
                    </el-tabs>
                </el-card>
            </template>
        </zdx-dialog>
    </div>
</template>

<style lang="scss" scoped></style>