<template>
	<div class="app-container">
		<el-tabs type="border-card" class="demo-tabs">
			<!-- 网站信息 -->
			<el-tab-pane>
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <Platform />
                        </el-icon>
                        <span>网站信息</span>
                    </span>
				</template>
				<el-form label-width="80px" :model="siteEntity" label-position="left">
					<el-row>
						<el-col :md="6">
							<el-form-item label="用户头像">
								<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
										   :show-file-list="false" accept="image/*"
										   :on-success="handleUserAvatarSuccess">
									<img v-if="siteEntity.userAvatar" :src="siteEntity.userAvatar" class="avatar" />
									<el-icon v-else class="avatar-uploader-icon">
										<Plus />
									</el-icon>
								</el-upload>
							</el-form-item>
						</el-col>
						<el-col :md="6">
							<el-form-item label="游客头像">
								<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
										   :show-file-list="false" accept="image/*"
										   :on-success="handleTouristAvatarSuccess">
									<img v-if="siteEntity.touristAvatar" :src="siteEntity.touristAvatar" class="avatar" />
									<el-icon v-else class="avatar-uploader-icon">
										<Plus />
									</el-icon>
								</el-upload>
							</el-form-item>
						</el-col>
					</el-row>
					<el-form-item label="网站名称">
						<el-input v-model="siteEntity.siteName" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item label="网站地址">
						<el-input v-model="siteEntity.siteAddress" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item label="网站简介">
						<el-input v-model="siteEntity.siteIntro" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item label="网站公告">
						<el-input style="width: 400px;" v-model="siteEntity.siteNotice"
								  :autosize="{ minRows: 4, maxRows: 5 }" resize="none" type="textarea"></el-input>
					</el-form-item>
					<el-form-item label="建站日期">
						<el-date-picker v-model="siteEntity.createSiteTime" value-format="YYYY-MM-DD" type="date"
										placeholder="选择日期"></el-date-picker>
					</el-form-item>
					<el-form-item label="备案号">
						<el-input v-model="siteEntity.recordNumber" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="handleUpdate">保 存</el-button>
					</el-form-item>
				</el-form>
			</el-tab-pane>
			<!-- 作者信息 -->
			<el-tab-pane label="author">
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <Flag />
                        </el-icon>
                        <span>作者信息</span>
                    </span>
				</template>
				<el-form label-width="80px" :model="siteEntity" label-position="left">
					<el-form-item label="作者头像">
						<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
								   :show-file-list="false" accept="image/*"
								   :on-success="handleAuthorAvatarSuccess">
							<img v-if="siteEntity.authorAvatar" :src="siteEntity.authorAvatar" class="avatar" />
							<el-icon v-else class="avatar-uploader-icon">
								<Plus />
							</el-icon>
						</el-upload>
					</el-form-item>
					<el-form-item label="网站作者">
						<el-input v-model="siteEntity.siteAuthor" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item label="关于我">
						<v-md-editor v-model="siteEntity.aboutMe" :disabled-menus="[]" :left-toolbar="toolList"
									 @upload-image="handleUploadImage" height="400px" />
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="handleUpdate">保 存</el-button>
					</el-form-item>
				</el-form>
			</el-tab-pane>
			<!-- 社交信息 -->
			<el-tab-pane label="social">
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <Opportunity />
                        </el-icon>
                        <span>社交信息</span>
                    </span>
				</template>
				<el-form label-width="70px" :model="siteEntity" label-position="left">
					<el-checkbox-group v-model="socialList">
						<el-form-item label="Github">
							<el-input v-model="siteEntity.github" style="width: 400px; margin-right: 1rem"></el-input>
							<el-checkbox label="github">是否展示</el-checkbox>
						</el-form-item>
						<el-form-item label="Gitee">
							<el-input v-model="siteEntity.gitee" style="width: 400px; margin-right: 1rem"></el-input>
							<el-checkbox label="gitee">是否展示</el-checkbox>
						</el-form-item>
						<el-form-item label="BiliBili">
							<el-input v-model="siteEntity.bilibili" style="width: 400px; margin-right: 1rem"></el-input>
							<el-checkbox label="bilibili">是否展示</el-checkbox>
						</el-form-item>
						<el-form-item label="QQ">
							<el-input v-model="siteEntity.qq" style="width: 400px; margin-right: 1rem"></el-input>
							<el-checkbox label="QQ">是否展示</el-checkbox>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="handleUpdate">保 存</el-button>
						</el-form-item>
					</el-checkbox-group>
				</el-form>
			</el-tab-pane>
			<!-- 审核&打赏 -->
			<el-tab-pane label="check">
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <Stamp />
                        </el-icon>
                        <span>审核&打赏</span>
                    </span>
				</template>
				<el-form label-width="100px" :model="siteEntity" label-position="left">
					<el-form-item label="评论审核">
						<el-radio-group v-model="siteEntity.commentCheck">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="留言审核">
						<el-radio-group v-model="siteEntity.messageCheck">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="打赏状态">
						<el-radio-group v-model="siteEntity.isReward">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-row style="width: 600px" v-if="siteEntity.isReward">
						<el-col :md="12">
							<el-form-item label="微信收款码">
								<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
										   :show-file-list="false" accept="image/*"
										   :on-success="handleWeiXinSuccess">
									<img v-if="siteEntity.weiXinCode" :src="siteEntity.weiXinCode" class="avatar" />
									<el-icon v-else class="avatar-uploader-icon">
										<Plus />
									</el-icon>
								</el-upload>
							</el-form-item>
						</el-col>
						<el-col :md="12">
							<el-form-item label="支付宝收款码">
								<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
										   :show-file-list="false" accept="image/*"
										   :on-success="handleAliSuccess">
									<img v-if="siteEntity.aliCode" :src="siteEntity.aliCode" class="avatar" />
									<el-icon v-else class="avatar-uploader-icon">
										<Plus />
									</el-icon>
								</el-upload>
							</el-form-item>
						</el-col>
					</el-row>
					<el-form-item>
						<el-button type="primary" @click="handleUpdate">保 存</el-button>
					</el-form-item>
				</el-form>
			</el-tab-pane>
			<!-- 其他设置 -->
			<el-tab-pane label="other">
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <Briefcase />
                        </el-icon>
                        <span>其他设置</span>
                    </span>
				</template>
				<el-form label-width="120px" :model="siteEntity" label-position="left">
					<el-form-item label="文章默认封面">
						<el-upload class="avatar-uploader" :headers="authorization" action="/api/zdx.file/upload"
								   :show-file-list="false" accept="image/*"
								   :on-success="handleArticleSuccess">
							<img v-if="siteEntity.articleCover" :src="siteEntity.articleCover" class="article-cover" />
							<el-icon v-else class="avatar-uploader-icon">
								<Plus />
							</el-icon>
						</el-upload>
					</el-form-item>
					<el-form-item label="邮箱通知">
						<el-radio-group v-model="siteEntity.emailNotice">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="音乐播放器">
						<el-radio-group v-model="siteEntity.isMusic">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="网易云歌单Id" v-if="siteEntity.isMusic">
						<el-input v-model="siteEntity.musicId" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item label="聊天室">
						<el-radio-group v-model="siteEntity.isChat">
							<el-radio :label="false">关闭</el-radio>
							<el-radio :label="true">开启</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="websocket链接" v-if="siteEntity.isChat">
						<el-input v-model="siteEntity.websocketUrl" style="width: 400px;"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="handleUpdate">保 存</el-button>
					</el-form-item>
				</el-form>
			</el-tab-pane>
			<el-tab-pane label="background">
				<template #label>
                    <span class="custom-tabs-label">
                        <el-icon>
                            <PictureFilled />
                        </el-icon>
                        <span>背景图片</span>
                    </span>
				</template>
				<el-form label-width="80px" :model="siteEntity" label-position="left">
					<el-col :md="6">
						<el-form-item label="首页背景">
							<el-upload class="avatar-uploader" multiple :headers="authorization" action="/api/zdx.file/upload"
									   :show-file-list="false" accept="image/*"
									   :on-success="handleBackgroundsSuccess">
								<div v-if="siteEntity.backgrounds">
									<img v-for="url in siteEntity.backgrounds" :src="url" class="article-cover" />
								</div>
								<el-icon v-else class="avatar-uploader-icon">
									<Plus />
								</el-icon>
							</el-upload>
						</el-form-item>
					</el-col>
					<el-form-item>
						<el-button type="primary" @click="handleUpdate">保 存</el-button>
					</el-form-item>
				</el-form>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {getToken} from "@/utils/auth";
import {uploadFile} from "@/api/tk/file";
import {updateSiteConfig, getSite} from "@/api/zdx/site";
import {ElMessage} from "element-plus";

const siteEntity = ref({})
const socialList = ref([])

const toolList = "undo redo clear | h bold italic strikethrough quote | ul ol table hr | link image code | emoji tip todo-list";
const authorization = computed(() => {
	return {
		Authorization: 'Bearer ' + getToken(),
	}
})

const handleUserAvatarSuccess = (res) => {
  siteEntity.value.userAvatar = res.data.fileUrl
}
const handleTouristAvatarSuccess = (res) => {
	siteEntity.value.touristAvatar = res.data.fileUrl
}
const handleAuthorAvatarSuccess = (res) => {
	siteEntity.value.authorAvatar = res.data.fileUrl
}

const handleBackgroundsSuccess = (res) => {
	if (siteEntity.value.backgrounds) {
		siteEntity.value.backgrounds.push(res.data.fileUrl)
	} else {
		siteEntity.value.backgrounds = []
		siteEntity.value.backgrounds.push(res.data.fileUrl)
	}
}
const handleWeiXinSuccess = (res) => {
	siteEntity.value.weiXinCode = res.data.fileUrl
}
const handleAliSuccess = (res) => {
	siteEntity.value.aliCode = res.data.fileUrl
}
const handleArticleSuccess = (res) => {
	siteEntity.value.articleCover = res.data.fileUrl
}

const handleUploadImage = (event, insertImage, files) => {
	files.forEach(file => {
		uploadFile(file, "ARTICLE").then(res => {
			insertImage({url: res.data.fileUrl})
		})
	})
}
const handleUpdate = () => {
	if (socialList.value.length > 0) {
		siteEntity.value.socialList = socialList.value.toString();
	} else {
		siteEntity.value.socialList = "";
	}
	updateSiteConfig(siteEntity.value).then(res => {
		ElMessage.success(res.message);
		doGetSite()
	})
}

const doGetSite = () => {
	getSite().then().then((res) => {
		siteEntity.value = res.data
		if (res.data.socialList) {
			socialList.value = res.data.socialList.split(",");
		}
	})
}
onMounted(() => {
	doGetSite()
})
</script>

<style scoped>
.demo-tabs > .el-tabs__content {
	padding: 32px;
	color: #6b778c;
	font-size: 32px;
	font-weight: 600;

}

.demo-tabs .custom-tabs-label .el-icon {
	vertical-align: middle;
}

.demo-tabs .custom-tabs-label span {
	vertical-align: middle;
	margin-left: 4px;
}

.avatar-uploader .avatar {
	width: 120px;
	height: 120px;
	object-fit: contain;
}

.article-cover {
	width: 300px;
}
</style>
