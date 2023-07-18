<script setup>
import {computed, onMounted, reactive, ref} from "vue";
import EmojiExtension from '@/components/EmojiExtension/index.vue';
import zdxDialog from "@/components/Dialog/index.vue"
import {toolbars} from '@/components/EmojiExtension/staticConfig';
import {MdEditor} from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {uploadFile} from "@/api/tk/file";
import {getToken} from "@/utils/auth";
import {getById, list, save} from "@/api/base";
import {ElMessage} from "element-plus";
import {useDict} from "@/utils/dict";
import {useRoute} from "vue-router";

const { zdx_article_type, zdx_article_status } = useDict('zdx_article_type', 'zdx_article_status')

const module = ref('article')
const editorRef = ref()
const formRef = ref()
const isDark = ref(false)
const dialog = ref(false)
const title = ref('')
const entity = ref({})
const categoryList = ref([])
const categoryName = ref()
const tagList = ref([])
const tagName = ref()

const rules = reactive({
	title: [{required: true, message: "标题不能为空", trigger: "blur"}],
	description: [{required: true, message: "简介不能为空", trigger: "blur"}],
	content: [{required: true, message: "内容不能为空", trigger: "blur"}],
	categoryName: [{required: true, message: "分类不能为空", trigger: "blur"}],
	tagNames: [{required: true, message: "标签不能为空", trigger: "blur"}]
})

const tagClass = computed(() => {
	return function (item) {
		if (entity.value.tagNames) {
			const index = entity.value.tagNames.indexOf(item);
			return index !== -1 ? "tag-item-select" : "tag-item";
		}
		return 'tag-item'
	}
})

const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken()
	}
})

const insert = (generator) => {
	editorRef.value?.insert(generator);
}
const openDialog = () => {
	dialog.value = true
	title.value = '新增文章'

}

const uploadImg = async (files, callback) => {
	const res = await Promise.all(
		files.map((file) => {
			return new Promise((resolve, reject) => {
				uploadFile(file, 'ARTICLE').then(res => {
					resolve(res.data)
				}).catch(err => {
					reject(err)
				})
			})
		})
	)
	callback(res.map(item => item.fileUrl))
}

const removeCategory = () => {
	entity.value.categoryName = "";
}

const addCategory = (name) => {
	entity.value.categoryName = name;
}

const searchCategory = (keyword, cb) => {
	const results = keyword
		? categoryList.value.filter(createCategoryFilter(keyword))
		: categoryList.value
	cb(results);
}

const createCategoryFilter = (queryString) => {
	return (restaurant) => {
		return (
			restaurant.categoryName.indexOf(queryString) !== -1
		)
	}
}

const saveCategory = () => {
	// 分类不为空
	if (categoryName.value.trim() !== "") {
		addCategory(categoryName.value);
		categoryName.value = "";
	}
}

const handleSelectCategory = (item) => {
	addCategory(item.categoryName);
}

const removeTag = (item) => {
	const index = entity.value.tagNames.indexOf(item);
	entity.value.tagNames.splice(index, 1);
}


const searchTag = (keyword, cb) => {
	const results = keyword
		? tagList.value.filter(createTagFilter(keyword))
		: tagList.value
	cb(results);
}

const createTagFilter = (queryString) => {
	return (restaurant) => {
		return (
			restaurant.name.indexOf(queryString) !== -1
		)
	}
}

const handleSelectTag = (item) => {
	addTag(item.name);
}

const addTag = (item) => {
	if (!entity.value.tagNames) {
		entity.value.tagNames = []
	}
	if (entity.value.tagNames.indexOf(item) === -1) {
		entity.value.tagNames.push(item);
	}
}

const saveTag = () => {
	if (tagName.value.trim() !== "") {
		addTag(tagName.value);
		tagName.value = "";
	}
}


const handleSuccess = (response) => {
	entity.value.cover = response.data.fileUrl;
}

const successHandle = (formEl) => {
	if (!formEl) return
	formEl.validate((valid, fields) => {
		if (valid) {
			if (Object.keys(entity.value.length !== 0)) {
				console.log(entity.value)
				save(module.value, entity.value).then(res => {
					ElMessage.success(res.message)
					dialog.value = false
				})
			}
		} else {
			console.log('error submit!', fields)
		}
	})
}

onMounted(() => {
	list('tag').then((res) => {
		tagList.value = res.data
	})
	list('category').then((res) => {
		categoryList.value = res.data
	})
	const articleId = useRoute().params.articleId
	if (articleId) {
		getById(module.value, articleId).then((res) => {
			entity.value = res.data
		})
	}
})


</script>

<template>
	<div class="app-container">
		<el-form :model="entity" :rules="rules" ref="formRef" label-width="80px">
			<el-row>
				<el-col :span="24">
					<div class="operation-container">
						<el-form-item label="文章标题" prop="title" style="width: 100%">
							<el-input type="textarea" v-model="entity.title" placeholder="请输入文章标题"
									  clearable/>
						</el-form-item>
						<el-button type="danger" style="margin-left: 10px" @click="openDialog">发布文章</el-button>
					</div>
				</el-col>
				<el-col :span="24">
					<el-form-item label="文章内容" prop="content">
						<md-editor ref="editorRef" v-model="entity.content" class="md-container"
								   :toolbars="toolbars" :theme="isDark ? 'dark' : 'light'"
								   placeholder="请输入文章内容..."
								   @on-upload-img="uploadImg"
						>
							<template #defToolbars>
								<EmojiExtension :onInsert="insert"/>
							</template>
						</md-editor>
					</el-form-item>
				</el-col>
			</el-row>
			<zdx-dialog :title="title" :dialog="dialog" @close="dialog = false" width="45%">
				<template #content>
					<el-row>
						<el-col :span="24">
							<el-form-item label="文章简介" prop="description" style="width: 100%">
								<el-input type="textarea" v-model="entity.description" placeholder="请输入文章简介"
										  clearable/>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="文章分类" prop="categoryName">
								<el-tag type="success" v-show="entity.categoryName" :disable-transitions="true"
										:closable="true" @close="removeCategory">
									{{ entity.categoryName }}
								</el-tag>
								<el-popover v-if="!entity.categoryName" placement="bottom-start" width="460"
											trigger="click">
									<template #reference>
										<el-button type="success" plain>添加分类</el-button>
									</template>
									<div class="popover-title">分类</div>
									<!-- 搜索框 -->
									<el-autocomplete style="width: 100%" v-model="categoryName"
													 :fetch-suggestions="searchCategory"
													 placeholder="请输入分类名搜索,enter可添加自定义分类"
													 :trigger-on-focus="false" @keyup.enter="saveCategory"
													 @select="handleSelectCategory">
										<template #default="{ item }">
											<div>{{ item.categoryName }}</div>
										</template>
									</el-autocomplete>
									<!-- 分类 -->
									<div class="popover-container">
										<div v-for="item of categoryList" :key="item.id" class="category-item"
											 @click="addCategory(item.name)">
											{{ item.name }}
										</div>
									</div>
								</el-popover>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="文章标签" prop="tagNames">
								<el-tag v-for="(item, index) of entity.tagNames" :key="index"
										:disable-transitions="true"
										:closable="true" @close="removeTag(item)" style="margin-right: 1rem;">
									{{ item }}
								</el-tag>
								<!-- 标签选项 -->
								<el-popover placement="bottom-start" width="460" trigger="click">
									<template #reference>
										<el-button type="success" plain>添加标签</el-button>
									</template>
									<div class="popover-title">标签</div>
									<!-- 搜索框 -->
									<el-autocomplete style="width: 100%" v-model="tagName"
													 :fetch-suggestions="searchTag"
													 placeholder="请输入标签名搜索,enter可添加自定义标签"
													 :trigger-on-focus="false" @keyup.enter="saveTag"
													 @select="handleSelectTag">
										<template #default="{ item }">
											<div>{{ item.name }}</div>
										</template>
									</el-autocomplete>
									<!-- 标签 -->
									<div class="popover-container">
										<div style="margin-bottom: 1rem">添加标签</div>
										<el-tag v-for="(item, index) of tagList" :key="index"
												:class="tagClass(item.name)"
												@click="addTag(item.name)" style="margin-right: 1rem;">
											{{ item.name }}
										</el-tag>
									</div>
								</el-popover>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="文章类型" prop="type">
								<el-select v-model="entity.type" placeholder="请选择" clearable
										   style="width: 70%">
									<el-option v-for="item in zdx_article_type" :key="item.value" :label="item.value"
											   :value="item.key"/>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="缩略图" prop="cover">
								<el-upload style="width: 70%" drag :show-file-list="false" :headers="authorization"
										   action="/api/zdx.file/upload"
										   accept="image/*" :on-success="handleSuccess">
									<el-icon class="el-icon--upload" v-if="!entity.cover">
										<upload-filled/>
									</el-icon>
									<div class="el-upload__text" v-if="!entity.cover">
										将文件拖到此处，或<em>点击上传</em>
									</div>
									<img v-else :src="entity.cover" width="360" alt=""/>
								</el-upload>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="置顶" prop="isTop">
								<el-radio-group v-model="entity.isTop">
									<el-radio :label="true">是</el-radio>
									<el-radio :label="false">否</el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="推荐" prop="isRecommend">
								<el-radio-group v-model="entity.isRecommend">
									<el-radio :label="true">是</el-radio>
									<el-radio :label="false">否</el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="24">
							<el-form-item label="发布形式" prop="status">
								<el-radio-group v-model="entity.status">
									<el-radio v-for="item in zdx_article_status" :label="item.key" :key="item.key">{{ item.value }}</el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
					</el-row>
				</template>
				<template #footer>
					<el-button type="primary" @click="successHandle(formRef)">
						保存
					</el-button>
				</template>
			</zdx-dialog>
		</el-form>
	</div>
</template>

<style lang="scss" scoped>
.operation-container {
	display: flex;
	align-items: center;
	margin-bottom: 1.25rem;
}

.md-container {
	min-height: 300px;
	height: calc(100vh - 200px);
}

.popover-title {
	margin-bottom: 1rem;
	text-align: center;
}

.popover-container {
	margin-top: 1rem;
	height: 260px;
	overflow-y: auto;
}

.category-item {
	cursor: pointer;
	padding: 0.6rem 0.5rem;
}

.category-item:hover {
	background-color: #f0f9eb;
	color: #67c23a;
}

.tag-item {
	margin-right: 1rem;
	margin-bottom: 1rem;
	cursor: pointer;
}

.tag-item-select {
	margin-right: 1rem;
	margin-bottom: 1rem;
	cursor: not-allowed;
	color: #ccccd8 !important;
}
</style>