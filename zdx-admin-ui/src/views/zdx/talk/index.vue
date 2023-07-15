<script setup>
import zdxPagination from "@/components/Pagination/index.vue";
import zdxDialog from "@/components/Dialog/index.vue"
import zdxEditor from "@/components/Editor/index.vue"
import emojiList from "@/utils/emoji";
import {computed, onMounted, reactive, ref} from "vue";
import {page, save} from "@/api/base";
import {getToken} from "@/utils/auth";
import {ElMessage} from "element-plus";

const queryParams = reactive({
	params: {
		status: undefined
	},
	page: 1,
	limit: 10
})

const module = ref('talk')
const previewList = ref([])
const talkList = ref([])
const loading = ref(false)
const total = ref(0)
const entity = ref({})
const dialog = ref(false)
const title = ref('')
const uploadList = ref([])
const statusList = ref([{key:true,value:'公开'}, {key: false, value:'私密'}])
const editorRef = ref()


const isActive = computed(() => {
	return function (value) {
		return queryParams.params.status === value ? "active-status" : "status";
	}
})

const statusTitle = computed(() => {
	let label = "公开";
	statusList.value.forEach(item => {
		if (item.value === entity.value.status) {
			label = item.label;
		}
	})
	return label;
})

const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken(),
	}
})


const changeStatus = (value) => {
	queryParams.page = 1;
	previewList.value = [];
	queryParams.params.status = value;
}

const pageTalk = () => {
	page(module.value, queryParams).then(res => {
		loading.value = true
		page(module.value, queryParams).then(res => {
			talkList.value = res.data.records
			loading.value = false
			total.value = parseInt(res.data.total)
			if (talkList.value) {
				talkList.value.forEach((item) => {
					if (item.imgList) {
						previewList.value.push(...item.imgList);
					}
				});
			}
		})
	})
}

const handleSuccess = (response) => {
	uploadList.value.push({ url: response.data.fileUrl });
}

const openModel = () => {
	title.value = '发布说说'
	entity.value = {}
	dialog.value = true
}

const handleUpdate = (row) => {
	title.value = '更改说说'
	entity.value = row
	dialog.value = true
}

const addEmoji = (key, value) => {
	editorRef.value.addText("<img src= '" +
		value +
		"' width='24'height='24' alt=" +
		key +
		" style='margin: 0 1px;vertical-align: text-bottom'/>");
}

const handleCommand = (command) => {
	entity.value.status = command;
}

const submitForm = () => {
	if (uploadList.value.length > 0) {
		let imgList = [];
		uploadList.value.forEach((item) => {
			imgList.push(item.url);
		});
		entity.value.images = JSON.stringify(imgList);
	} else {
		entity.value.images = "";
	}
	if (Object.keys(entity.value).length > 0) {
		save(module.value, entity.value).then(res => {
			ElMessage.success(res.message)
			dialog.value = false
			pageTalk()
		})
	}
}

onMounted(() => {
	pageTalk()
})
</script>

<template>
	<div class="app-container">
		<!-- 操作 -->
		<el-row :gutter="10" class="mb15">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="Promotion" @click="openModel">发布说说</el-button>
			</el-col>
		</el-row>
		<!-- 说说状态 -->
		<el-row :gutter="24" style="color: #999;">
			<el-col :span="1.5">
				状态
			</el-col>
			<el-col :span="1.5" :class="isActive(undefined)" @click="changeStatus(undefined)">
				全部
			</el-col>
			<el-col :span="1.5" :class="isActive(true)" @click="changeStatus(true)">
				公开
			</el-col>
			<el-col :span="1.5" :class="isActive(false)" @click="changeStatus(false)">
				私密
			</el-col>
		</el-row>
		<el-empty v-if="talkList.length === 0" description="暂无说说"></el-empty>

		<!-- 说说列表 -->
		<div class="talk-item" v-for="talk of talkList" :key="talk.id">
			<!-- 用户头像 -->
			<img class="user-avatar" :src="talk.avatar">
			<div class="talk-content-container">
				<!-- 用户信息 -->
				<div class="user-info">
					<!-- 用户昵称 -->
					<div class="user-name">{{ talk.nickname }}</div>
					<!-- 操作 -->
					<el-dropdown trigger="click">
						<el-icon style="color:#333">
							<MoreFilled/>
						</el-icon>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="handleUpdate(talk)">编辑</el-dropdown-item>
								<el-dropdown-item @click="handleDelete(talk.id)">删除</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</div>
				<!-- 说说信息 -->
				<div class="talk-info">
					<!-- 发布时间 -->
					<span class="talk-time">{{ talk.createTime }}</span>
					<span class="top" v-if="talk.isTop"><svg-icon icon-class="top"
																  size="0.85rem"></svg-icon>置顶</span>
					<span class="secret" v-if="talk.status">
                        <el-icon>
                            <Hide/>
                        </el-icon>
                        私密</span>
				</div>
				<!-- 说说内容 -->
				<div class="talk-content" v-html="talk.content"></div>
				<!-- 说说图片 -->
				<el-row :gutter="4" style="margin-top: 0.5rem;">
					<el-col :md="8" :cols="6" v-for="(img, index) of talk.imgList" :key="index">
						<el-image class="talk-image" :src="img" :preview-src-list="previewList"></el-image>
					</el-col>
				</el-row>
			</div>
		</div>
		<zdx-pagination v-show="total > 0" :total="total" v-model:page="queryParams.page"
						v-model:limit="queryParams.limit"
						@pagination="pageTalk"/>

		<zdx-dialog :title="title" :dialog="dialog" @close="dialog = false" :is-open-close="false">
			<template #content>
				<el-form :model="entity">
					<el-form-item prop="content">
						<zdx-Editor ref="editorRef" v-model:text="entity.content" placeholder="说点什么吧"></zdx-Editor>
					</el-form-item>
				</el-form>
				<el-row :gutter="10" align="middle" style="margin-top: 16px;">
					<el-col :span="1.5">
						<!-- 表情 -->
						<el-popover placement="bottom-start" :width="460" trigger="click">
							<template #reference>
								<span><svg-icon icon-class="emoji" size="1.4rem" color="#838383"></svg-icon></span>
							</template>
							<span class="emoji-item" v-for="(value, key, index) of emojiList" :key="index"
								  @click="addEmoji(key, value)">
                            <img :src="value" :title="key" class="emoji" width="24" height="24" alt=""/>
                        </span>
						</el-popover>
					</el-col>
					<el-col :span="1.5">
						<!-- 图片上传 -->
						<el-upload accept="image/*" multiple action="/api/zdx.file/upload" :headers="authorization"
								   	 :on-success="handleSuccess" :show-file-list="false">
							<svg-icon icon-class="album" size="1.5rem" color="#838383"
									  style="padding-top:0.1rem;"></svg-icon>
						</el-upload>
					</el-col>
					<el-col :span="1.5" :offset="14">
						<el-switch style="margin-right:7px" v-model="entity.isTop" inactive-text="置顶"
								   :active-value="true"
								   :inactive-value="false"/>
					</el-col>
					<el-col :span="1.5">
						<el-dropdown trigger="click">
                        <span class="el-dropdown-link">
                            {{ statusTitle }}
                            <el-icon class="el-icon--right">
                                <arrow-down/>
                            </el-icon>
                        </span>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item v-for="(item, index) of statusList" :key="index"
													  @click="handleCommand(item.key)">
										{{ item.value }}
									</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</el-col>
					<el-col :span="1.5">
						<el-button type="primary" :disabled="entity.content === ''" @click="submitForm">发布
						</el-button>
					</el-col>
				</el-row>
				<!-- 图片上传 -->
				<el-upload accept="image/*" v-show="uploadList.length > 0" action="/api/admin/talk/upload"
						   :headers="authorization" list-type="picture-card" :file-list="uploadList" multiple
						   :before-upload="beforeUpload" :on-success="handleSuccess" :on-remove="handleRemove"
						   :on-preview="handlePictureCardPreview" style="margin-top:1rem;">
					<el-icon>
						<Plus/>
					</el-icon>
				</el-upload>
			</template>
		</zdx-dialog>
	</div>
</template>

<style lang="scss" scoped>
.el-dropdown-link {
	display: flex;
	align-items: center;
	margin-top: 0.1rem;
	cursor: pointer;
}

.status {
	cursor: pointer;
}

.active-status {
	cursor: pointer;
	color: #333;
	font-weight: bold;
}

.talk-item {
	display: flex;
	padding: 1rem 1.25rem;
	border-radius: 0.5rem;
	box-shadow: 0 0.625rem 1.875rem -0.9375rem rgba(0, 0, 0, 0.1);
	transition: all 0.2s ease-in-out 0s;

	&:hover {
		box-shadow: 0 0 2rem rgba(0, 0, 0, 0.1);
	}

	&:not(:first-child) {
		margin-top: 1.25rem;
	}
}

.user-avatar {
	width: 2.25rem;
	height: 2.25rem;
	border-radius: 50%;
	transition: all 0.5s;

	&:hover {
		transform: rotate(360deg)
	}
}

.talk-content-container {
	flex: auto;
	margin-left: 0.825rem;
}

.user-info {
	display: flex;
	align-items: center;
	justify-content: space-between;

	.user-name {
		font-size: 0.875rem;
		font-weight: 600;
	}
}

.talk-info {
	display: flex;
	align-items: center;
	margin-top: 0.2rem;
	line-height: 1.5rem;

	.talk-time {
		font-size: 0.8125rem;
		margin-right: 1.25rem;
		color: #9499a0;
	}

	.top {
		font-size: 0.8125rem;
		color: #ff7242;
	}

	.secret {
		color: #999;
		margin-left: 10px;
	}
}

.talk-content {
	margin-top: 0.5rem;
	font-size: 0.875rem;
	line-height: 1.5rem;
	word-wrap: break-word;
	word-break: break-all;
	white-space: pre-line;
}

.talk-image {
	width: 100%;
	height: 200px;
	padding: 0.125rem;
	border-radius: 10px;
	cursor: pointer;
	object-fit: cover;
}

.emoji-item {
	cursor: pointer;
	display: inline-block;

	.emoji {
		user-select: none;
		margin: 0.25rem;
		display: inline-block;
		vertical-align: middle;
	}

	&:hover {
		transition: all 0.2s;
		border-radius: 0.25rem;
		background: #dddddd;
	}
}
</style>