<script setup>
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import {computed, onMounted, reactive, ref} from "vue";
import {batchDel, del, getById, page, save} from "@/api/base";
import {useRoute} from "vue-router";
import {getToken} from "@/utils/auth";
import {addPhoto} from "@/api/zdx/photo";
import {ElMessage, ElMessageBox} from "element-plus";

const queryParams = reactive({
	params: {
		albumId: useRoute().params.albumId
	},
	page: 1,
	limit: 10
})

const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken(),
	}
})

const module = ref('photo')
const albumInfo = ref()
const photoFormRef = ref()
const upload = ref(false)
const update = ref(false)
const selectPhotoIdList = ref([])
const checkAll = ref(false)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')
const photoIdList = ref([])
const photoList = ref([])
const isIndeterminate = ref(false)
const entity = ref({})
const loading = ref(false)
const total = ref(0)
const uploadList = ref([])

const handleCheckedPhotoChange = (value) => {
	const checkedCount = value.length;
	checkAll.value = checkedCount === photoIdList.value.length;
	isIndeterminate.value = checkedCount > 0 && checkedCount < photoIdList.value.length;
}

const handleCheckAllChange = (val) => {
	selectPhotoIdList.value = val ? photoIdList.value : [];
	isIndeterminate.value = false;
}

const handleCommand = (photo) => {
	photoFormRef.value.resetFields();
	update.value = true;
	entity.value = photo;

}

const pagePhoto = () => {
	loading.value = true
	page(module.value, queryParams).then(res => {
		photoList.value = res.data.records
		loading.value = false
		total.value = parseInt(res.data.total)
	})
}
const handleRemove = (file) => {
	uploadList.value.forEach((item, index) => {
		if (item.url === file.url) {
			uploadList.value.splice(index, 1);
		}
	})
}
const handlePictureCardPreview = (file) => {
	dialogImageUrl.value = file.url
	dialogVisible.value = true;
};
const handleSuccess = (response) => {
	uploadList.value.push({ url: response.data.fileUrl })
}

const handleAdd = () => {
	let photoUrlList = [];
	if (uploadList.value.length > 0) {
		uploadList.value.forEach(item => {
			photoUrlList.push(item.url);
		});
	}
	addPhoto({ albumId: queryParams.params.albumId, photoUrlList: photoUrlList }).then((res) => {
		ElMessage.success(res.message)
		pagePhoto()
		upload.value = false;
	})
}

const submitForm = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				save(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					update.value = false
					pagePhoto()
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}

const handleDelete = () => {
	ElMessageBox.confirm('确定删除所选数据吗？', '删除', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		batchDel(module.value, selectPhotoIdList.value).then(res => {
			ElMessage.success(res.message)
			pagePhoto()
		})
	})
}

onMounted(() => {
	pagePhoto()
	getById("album", queryParams.params.albumId).then((res) => {
		albumInfo.value = res.data
	})
})
</script>

<template>
	<div class="app-container">
		<!-- 相册信息 -->
		<el-row :gutter="12" class="mb15" v-if="albumInfo">
			<el-col :span="1.5">
				<el-image fit="cover" class="album-cover" :src="albumInfo.cover">
				</el-image>
			</el-col>
			<el-col :span="12">
				<el-row align="bottom">
					<span class="album-name">{{ albumInfo.name }}</span>
					<span class="photo-count">{{ albumInfo.photoCount }}张</span>
				</el-row>
				<el-row class="album-desc">{{ albumInfo.description }}</el-row>
				<el-row class="select-count">已选择{{ selectPhotoIdList.length }}张</el-row>
			</el-col>
		</el-row>
		<!-- 操作按钮 -->
		<el-row :gutter="10" class="mb20">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Upload" @click="upload = true">上传</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="success" plain icon="Promotion"
						   :disabled="selectPhotoIdList.length === 0">移动</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="Delete" :disabled="selectPhotoIdList.length === 0"
						   @click="handleDelete">批量删除</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">
					全选
				</el-checkbox>
			</el-col>
		</el-row>

		<!-- 照片列表 -->
		<el-checkbox-group v-model="selectPhotoIdList" @change="handleCheckedPhotoChange">
			<el-row class="picture-list" :gutter="10">
				<el-col :xs="12" :sm="6" :lg="4" v-for="photo of photoList" :key="photo.id" style="margin-bottom:1rem;">
					<el-checkbox :label="photo.id">
						<template #default>
							<div class="photo-item">
								<div class="photo-operation">
									<el-dropdown>
										<el-icon style="color:#fff">
											<MoreFilled />
										</el-icon>
										<template #dropdown>
											<el-dropdown-menu>
												<el-dropdown-item @click="handleCommand(photo)">编辑</el-dropdown-item>
											</el-dropdown-menu>
										</template>
									</el-dropdown>
								</div>
								<el-image class="photo-cover" fit="cover" :src="photo.url"
										  :preview-src-list="[photo.url]">
								</el-image>
								<div class="photo-name">{{ photo.name }}</div>
							</div>
						</template>
					</el-checkbox>
				</el-col>
			</el-row>
		</el-checkbox-group>

		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page" v-model:limit="queryParams.limit"
						@pagination="pagePhoto" />

		<zdx-dialog title="更改图片" :dialog="update" @close="update = false">
			<template #content>
				<el-form ref="photoFormRef" label-width="100px" :model="entity">
					<el-form-item label="照片名称" prop="name">
						<el-input placeholder="请输入照片名称" v-model="entity.name" style="width: 250px;" />
					</el-form-item>
					<el-form-item label="照片描述" prop="description">
						<el-input placeholder="请输入照片描述" v-model="entity.description" style="width: 250px;" />
					</el-form-item>
				</el-form>
			</template>
			<template #footer>
				<el-button type="primary" @click="submitForm(photoFormRef)">确 定</el-button>
			</template>
		</zdx-dialog>

		<zdx-dialog title="上传图片" :dialog="upload" @close="upload = false" :is-open-close="false">
			<template #content>
				<div class="upload-container">
					<el-upload v-show="uploadList.length > 0" :headers="authorization" class="avatar-uploader" multiple
							   action="/api/zdx.file/upload" :on-success="handleSuccess"
							   :on-remove="handleRemove" :on-preview="handlePictureCardPreview" list-type="picture-card"
							   :file-list="uploadList" accept="image/*">
						<img class="avatar"  alt=""/>
						<el-icon class="avatar-uploader-icon">
							<Plus />
						</el-icon>
					</el-upload>
					<div class="upload">
						<el-upload v-show="uploadList.length === 0" :headers="authorization" drag multiple
								   action="/api/zdx.file/upload"  :show-file-list="false"
								   accept="image/*" :on-success="handleSuccess" style="width:360px;">
							<el-icon class="el-icon--upload"><upload-filled /></el-icon>
							<div class="el-upload__text">
								将文件拖到此处，或<em>点击上传</em>
							</div>
							<img width="360"  alt=""/>
						</el-upload>
					</div>
				</div>
			</template>
			<template #footer>
				<div class="dialog-footer">
					<div>
						共上传{{ uploadList.length }}张照片
					</div>
					<div>
						<el-button type="primary" :disabled="uploadList.length === 0" @click="handleAdd">确 定</el-button>
						<el-button @click="upload = false">返回</el-button>
					</div>
				</div>
			</template>
		</zdx-dialog>

		<el-dialog v-model="dialogVisible" append-to-body>
			<img :src="dialogImageUrl" style="max-width:100%"  alt=""/>
		</el-dialog>
	</div>
</template>

<style scoped lang="scss">
.album-cover {
	border-radius: 4px;
	width: 5rem;
	height: 5rem;
}

.album-name {
	font-size: 1.25rem;
}

.photo-count {
	font-size: 13px;
	margin: 0 0 0.1rem 0.5rem;
}

.album-desc {
	font-size: 15px;
	margin-top: 0.4rem;
}

.select-count {
	font-size: 13px;
	margin-top: 0.4rem;
}

.photo-item {
	position: relative;
	width: 100%;
	cursor: pointer;

	margin-bottom: 1rem;

	.photo-operation {
		position: absolute;
		top: 0.3rem;
		right: 0.5rem;
		z-index: 9;
	}

	.photo-cover {
		width: 100%;
		height: 7rem;
		border-radius: 4px;
	}

	.photo-name {
		font-size: 14px;
		margin-top: 0.3rem;
		text-align: center;
	}
}

.upload-container {
	height: 400px;

	.upload {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}
}

.dialog-footer {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.picture-list .el-checkbox {
	display: inline-block !important;
}

.picture-list .el-checkbox__input {
	position: absolute !important;
	top: 0.3rem;
	left: 0.8rem;
}
</style>