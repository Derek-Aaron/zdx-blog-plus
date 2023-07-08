<script setup>
import zdxRightToolbar from "@/components/RightToolbar/index.vue";
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDictTag from "@/components/DictTag/index.vue"
import zdxDialog from "@/components/Dialog/index.vue"
import { onMounted, reactive, ref } from "vue";
import { page, batchDel, save, list } from '@/api/base'
import { useDict } from "@/utils/dict";
import { ElMessage, ElMessageBox } from "element-plus";
import { updateUserStatus, resetPassword } from "@/api/us/user"
import { getRoleIds } from "@/api/us/role"

const { zdx_dict_gender } = useDict('zdx_dict_gender')

const showSearch = ref(true)
const module = ref('user')
const queryRef = ref()
const userRef = ref()
const pwdRef = ref()
const loading = ref(false)
const dialog = ref(false)
const openPwd = ref(false)
const openRole = ref(false)
const userList = ref([])
const total = ref(0)
const ids = ref(0)
const entity = ref({})
const title = ref('')
const entityPwd = ref({})
const entityId = ref('')
const roleData = ref([])
const roleIds = ref([])


const queryParams = reactive({
   params: {
      username: '',
      nickname: '',
      email: '',
      mobile: '',
      personId: '',
   },
   page: 1,
   limit: 10
})

const columns = reactive([
   { key: 0, label: `用户编号`, visible: true },
   { key: 1, label: `用户名称`, visible: true },
   { key: 2, label: `用户昵称`, visible: true },
   { key: 3, label: `性别`, visible: true },
   { key: 4, label: `手机号码`, visible: true },
   { key: 5, label: `是否禁用`, visible: true },
   { key: 6, label: `是否锁定`, visible: true },
   { key: 7, label: `创建时间`, visible: true }
])

const equalToPassword = (rule, value, callback) => {
   if (user.newPassword !== value) {
      callback(new Error("两次输入的密码不一致"));
   } else {
      callback();
   }
};

const rules = reactive({
   username: [{ required: true, message: "用户名称不能为空", trigger: "blur" }, { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur" }],
   nickname: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
   email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
   mobile: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
})

const pwdRules = ref({
   newPassword: [{ required: true, message: "新密码不能为空", trigger: "blur" }, { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }],
   confirmPassword: [{ required: true, message: "确认密码不能为空", trigger: "blur" }, { required: true, validator: equalToPassword, trigger: "blur" }]
});

const resetQuery = () => {
   queryRef.value.resetFields()
}

const handleSelectionChange = (selection) => {
   ids.value = selection.map(item => item.id);
   if (selection.length === 1) {
      entity.value = selection[0]
   }
}

const pageUser = () => {
   loading.value = true
   page(module.value, queryParams).then((res) => {
      userList.value = res.data.records
      loading.value = false
      total.value = parseInt(res.data.total)
   })
}

const handleChange = (row, type) => {
   updateUserStatus({
      userId: row.id,
      isDisabled: row.isDisabled,
      isLocked: row.isLocked,
      type: type
   }).then((res) => {
      ElMessage.success(res.message)
   })
}

const handleAdd = () => {
   rules['password'] = [{ required: true, message: "用户密码不能为空", trigger: "blur" }]
   dialog.value = true
   title.value = '新增用户'
   entity.value = {}
}

const handleUpdate = (row) => {
   if (row.id) {
      entity.value = row
   }
   if (Object.keys(entity.value).length === 0) {
      ElMessage.error('请选择实例')
      return
   }
   rules['password'] = []

   dialog.value = true
   title.value = '编辑用户【' + entity.value.nickname + '】'

}

const handleDelete = (id) => {
   ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
   }).then(() => {
      let array = []
      if (id instanceof String && id) {
         array.push(id)
      }
      if (ids.value.length !== 0) {
         array = ids.value
      }
      batchDel(module.value, array).then((res) => {
         ElMessage.success(res.message);
         pageUser()
      })
   })

}

const handleResetPwd = (id) => {
   entityId.value = id
   openPwd.value = true
}

const handleAuthRole = (id) => {
   getRoleIds(id).then(res => {
      roleIds.value = res.data
      openRole.value = true
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
               pageUser()
            })
         }
      } else {
         console.log('error submit!', fields)
      }
   })

}

const resetPwd = (formEl) => {
   if (!formEl) return
   formEl.validate((valid, fields) => {
      if (valid) {
         resetPassword({
            newPassword: entityPwd.value.newPassword,
            userId: entityId.value
         }).then((res) => {
            ElMessage.success(res.message)
            openPwd.value = false
         })
      } else {
         console.log('error submit!', fields)
      }
   })
}

const roleChange = (val, direction, move) => {

}

onMounted(() => {
   pageUser()
   list('role').then(res => {
      roleData.value = res.data
   })
})
</script>

<template>
   <div class="app-container">
      <!--用户数据-->
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
         <el-form-item label="用户名称" prop="username">
            <el-input v-model="queryParams.params.username" placeholder="请输入用户名称" clearable style="width: 240px"
               @keyup.enter="pageUser" @clear="pageUser" />
         </el-form-item>
         <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="queryParams.params.nickname" placeholder="请输入用户昵称" clearable style="width: 240px"
               @keyup.enter="pageUser" @clear="pageUser" />
         </el-form-item>
         <el-form-item label="用户邮箱" prop="email">
            <el-input v-model="queryParams.params.email" placeholder="请输入用户邮箱" clearable style="width: 240px"
               @keyup.enter="pageUser" @clear="pageUser" />
         </el-form-item>
         <el-form-item label="手机号" prop="mobile">
            <el-input v-model="queryParams.params.mobile" placeholder="请输入手机号" clearable style="width: 240px"
               @keyup.enter="pageUser" @clear="pageUser" />
         </el-form-item>
         <el-form-item label="personId" prop="personId">
            <el-input v-model="queryParams.params.personId" placeholder="请输入personId" clearable style="width: 240px"
               @keyup.enter="pageUser" @clear="pageUser" />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="pageUser">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
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
            <el-button type="danger" plain icon="Delete" :disabled="!ids.length > 0" @click="handleDelete">删除</el-button>
         </el-col>
         <zdx-right-toolbar v-model:showSearch="showSearch" @queryTable="pageUser" :columns="columns"></zdx-right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="50" align="center" />
         <el-table-column label="用户编号" align="center" key="id" prop="id" show-overflow-tooltip v-if="columns[0].visible" />
         <el-table-column label="用户名称" align="center" key="username" prop="username" v-if="columns[1].visible"
            show-overflow-tooltip />
         <el-table-column label="用户昵称" align="center" key="nickname" prop="nickname" v-if="columns[2].visible"
            show-overflow-tooltip />
         <el-table-column label="性别" align="center" key="gender" prop="gender" v-if="columns[3].visible"
            show-overflow-tooltip>
            <template #default="scope">
               <zdx-dict-tag :options="zdx_dict_gender" :value="scope.row.gender" />
            </template>
         </el-table-column>
         <el-table-column label="手机号码" align="center" key="mobile" prop="mobile" v-if="columns[4].visible" width="120" />
         <el-table-column label="是否禁用" align="center" key="isDisabled" v-if="columns[5].visible">
            <template #default="scope">
               <el-switch v-model="scope.row.isDisabled" class="ml-2" :active-value="false" :inactive-value="true"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  @change="handleChange(scope.row, 'disabled')"></el-switch>
            </template>
         </el-table-column>
         <el-table-column label="是否锁定" align="center" key="isLocked" v-if="columns[6].visible">
            <template #default="scope">
               <el-switch v-model="scope.row.isLocked" class="ml-2" :active-value="false" :inactive-value="true"
                  style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  @change="handleChange(scope.row, 'locked')"></el-switch>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[7].visible" show-overflow-tooltip />
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
                        <el-tooltip content="重置密码" placement="top">
                           <el-button link type="primary" icon="Key" @click="handleResetPwd(scope.row.id)" />
                        </el-tooltip>
                     </el-dropdown-item>
                     <el-dropdown-item>
                        <el-tooltip content="分配角色" placement="top">
                           <el-button link type="primary" icon="CircleCheck" @click="handleAuthRole(scope.row.id)" />
                        </el-tooltip>
                     </el-dropdown-item>
                  </template>
               </el-dropdown>
            </template>
         </el-table-column>
      </el-table>
      <zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
         @pagination="pageUser" />
      <zdx-dialog :dialog="dialog" @close="dialog = false" :title="title" width="50%" @success="successHandle">
         <template #content>
            <el-form :model="entity" :rules="rules" ref="userRef" label-width="80px">
               <el-row>
                  <el-col :span="12">
                     <el-form-item label="用户名" prop="username">
                        <el-input v-model="entity.username" placeholder="请输入用户名" maxlength="30" clearable />
                     </el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="entity.nickname" placeholder="请输入用户昵称" maxlength="30" clearable />
                     </el-form-item>
                  </el-col>
               </el-row>
               <el-row>
                  <el-col :span="12">
                     <el-form-item label="密码" prop="password">
                        <el-input v-model="entity.password" placeholder="请输入密码" maxlength="11" clearable show-password/>
                     </el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="手机号码" prop="mobile">
                        <el-input v-model="entity.mobile" placeholder="请输入手机号码" maxlength="11" clearable />
                     </el-form-item>
                  </el-col>
               </el-row>
               <el-row>
                  <el-col :span="12">
                     <el-form-item label="邮箱" prop="email">
                        <el-input v-model="entity.email" placeholder="请输入邮箱" maxlength="50" />
                     </el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="用户性别">
                        <el-select v-model="entity.gender" placeholder="请选择">
                           <el-option v-for="dict in zdx_dict_gender" :key="dict.key" :label="dict.value"
                              :value="dict.key"></el-option>
                        </el-select>
                     </el-form-item>
                  </el-col>
               </el-row>
               <el-row>
                  <el-col :span="12">
                     <el-form-item label="是否禁用">
                        <el-radio-group v-model="entity.isDisabled">
                           <el-radio :label="true">是</el-radio>
                           <el-radio :label="false">否</el-radio>
                        </el-radio-group>
                     </el-form-item>
                  </el-col>
                  <el-col :span="12">
                     <el-form-item label="是否锁定">
                        <el-radio-group v-model="entity.isLocked">
                           <el-radio :label="true">是</el-radio>
                           <el-radio :label="false">否</el-radio>
                        </el-radio-group>
                     </el-form-item>
                  </el-col>
               </el-row>
               <el-row>
                  <el-col :span="24">
                     <el-form-item label="备注">
                        <el-input v-model="entity.description" type="textarea" placeholder="请输入内容"></el-input>
                     </el-form-item>
                  </el-col>
               </el-row>
            </el-form>
         </template>
         <template #footer>
            <el-button type="primary" @click="successHandle(userRef)">
               保存
            </el-button>
         </template>
      </zdx-dialog>
      <zdx-dialog title="重置密码" :dialog="openPwd" @close="openPwd = false" @success="resetPwd">
         <template #content>
            <el-form :model="entityPwd" :rules="pwdRules" ref="pwdRef" label-width="80px">
               <el-row>
                  <el-col :span="24">
                     <el-form-item label="密码" prop="newPassword">
                        <el-input type="password" v-model="entityPwd.newPassword" placeholder="请输入密码" maxlength="50"
                           show-password clearable />
                     </el-form-item>
                  </el-col>
               </el-row>
               <el-row>
                  <el-col :span="24">
                     <el-form-item label="确认密码" prop="confirmPassword">
                        <el-input type="password" v-model="entityPwd.confirmPassword" placeholder="请输入确认密码" maxlength="50"
                           show-password clearable />
                     </el-form-item>
                  </el-col>
               </el-row>
            </el-form>
         </template>
         <template #footer>
            <el-button type="primary" @click="resetPwd(pwdRef)">
               提交
            </el-button>
         </template>
      </zdx-dialog>
      <zdx-dialog title="添加角色" width="35%" :dialog="openRole" @close="openRole = false">
         <template #content>
            <el-row>
               <el-col :span="24">
                  <el-transfer :titles="['全部', '已有']" filterable filter-placeholder="请选择" v-model="roleIds"
                     :data="roleData" :props="{ key: 'id', label: 'name' }" @change="dataChange" />
               </el-col>
            </el-row>
         </template>
      </zdx-dialog>
   </div>
</template>

<style scoped lang="scss"></style>