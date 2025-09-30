
<template>
  <el-config-provider :locale="zhCn" :message="config">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { Api } from "@/utils/Api.js";
import VueCookies from "vue-cookies";
import Request from "@/utils/Request";
import { useLoginStore } from "@/stores/loginStore.js";
import { useSysSettingStore } from "@/stores/sysSettingStore.js";
import { useCategoryStore } from "@/stores/categoryStore.js";
import { useSearchHistoryStore } from "@/stores/searchHisotryStore.js";
import { useRoute, useRouter } from "vue-router";

import FingerprintJS from "@fingerprintjs/fingerprintjs";

const loginStore = useLoginStore();

const sysSettingStore = useSysSettingStore();

const categoryStore = useCategoryStore();

const searchHistoryStore = useSearchHistoryStore();

const route = useRoute();
const router = useRouter();

const config = ref({
  max: 1,
});

//自动登录
const autoLogin = async () => {
  const token = VueCookies.get("token");
  if (!token) {
    return;
  }
  let result = await Request({
    url: Api.autoLogin,
  });
  if (!result) {
    return;
  }
  saveLoginInfo(result.data);
};
const saveLoginInfo = (loginInfo) => {
  if (!loginInfo) {
    loginStore.saveUserInfo({});
  } else {
    loginStore.saveUserInfo(loginInfo);
  }
};

//获取系统设置信息
const getSysSetting = async () => {
  let result = await Request({
    url: Api.getSysSetting,
  });
  if (!result) {
    return;
  }
  sysSettingStore.saveSetting(result.data);
};

let categoryList = [];
let categoryMap = {};

//获取分类
const loadCategory = async () => {
  categoryStore.saveCategoryMap({})
  let result = await Request({
    url: Api.loadAllCategory,
  });
  if (!result) {
    return;
  }
  categoryList = result.data;

  result.data.forEach((element) => {
    categoryMap[element.categoryCode] = element;
    element.children.forEach((sub) => {
      categoryMap[sub.categoryCode] = sub;
    });
  });
  categoryStore.saveCategoryMap(categoryMap);
  categoryStore.saveCategoryList(categoryList);
};

const getDeviceId = async () => {
  let deviceId = VueCookies.get("deviceId");
  if (!deviceId) {
    const fpPromise = await FingerprintJS.load();
    const result = await fpPromise.get();
    deviceId = result.visitorId;
    VueCookies.set("deviceId", deviceId, -1);
  }
  loginStore.saveDeviceId(deviceId);
};

onBeforeMount(() => {
  getDeviceId();
  autoLogin();
  getSysSetting();
  loadCategory();
  searchHistoryStore.initHistory();
});
</script>

<style scoped>
</style>
