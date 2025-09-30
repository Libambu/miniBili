import '@/assets/scss/base.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/icon/iconfont.css'
import App from './App.vue'
import router from './router'
import VueCookies from 'vue-cookies'


import Collapse from "@/components/Collapse.vue";
import Avatar from "@/components/Avatar.vue"
import Dialog from "@/components/Dialog.vue";
import ImageCoverSelect from "@/components/ImageCoverSelect.vue";
import DataList from "@/components/DataList.vue";
import DataGridList from "@/components/DataGridList.vue";
import NoData from "@/components/NoData.vue";
import Cover from "@/components/Cover.vue";
import MyTab from "@/components/MyTab.vue";
import DataLoadMoreList from '@/components/DataLoadMoreList.vue'
import VideoItem from "@/components/VideoItem.vue"

import Request from "@/utils/Request"
import Message from "@/utils/Message"
import Utils from "@/utils/Utils"
import Verify from "@/utils/Verify"
import Confirm from "@/utils/Confirm.js"

import { Api } from "@/utils/Api.js"

const app = createApp(App)
app.use(ElementPlus)
app.use(createPinia())
app.use(router)

app.component("Collapse", Collapse);
app.component("Avatar", Avatar);
app.component("Cover", Cover);
app.component("Dialog", Dialog);
app.component("ImageCoverSelect", ImageCoverSelect);
app.component("DataList", DataList);
app.component("NoData", NoData);
app.component("MyTab", MyTab);
app.component("VideoItem", VideoItem);
app.component("DataGridList", DataGridList);
app.component("DataLoadMoreList", DataLoadMoreList);

app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.Request = Request;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Utils = Utils;
app.config.globalProperties.Api = Api
app.config.globalProperties.Verify = Verify
app.config.globalProperties.Confirm = Confirm;
//页面最大宽度
app.config.globalProperties.bodyMaxWidth = 2000;
//页面最小宽度
app.config.globalProperties.bodyMinWidth = 1250;
//顶部分类，一行多少个分类
app.config.globalProperties.rowCategoryCount = 10;
//分片大小
app.config.globalProperties.chunkSize = 1024 * 512
//最多同时上传数量
app.config.globalProperties.maxUploading = 3
//视频后缀
app.config.globalProperties.videoAccept = ".mp4,.avi,.rmvb,.mkv,.mov";
//图片后缀
app.config.globalProperties.imageAccept = ".jpg,.png,.gif,.bmp,.webp";
//缩略图后缀
app.config.globalProperties.imageThumbnailSuffix = "_thumbnail.jpg";
//轮播最多显示的视频数
app.config.globalProperties.carouselMaxCount = 5;
//bodyPaddingWidth
app.config.globalProperties.bodyPadding = 150
app.mount('#app')
