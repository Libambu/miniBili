<template>
  <div class="body-container">
    <div class="body-title">
      {{ route.name == "uhomeFocus" ? "我的关注" : "我的粉丝" }}
    </div>
    <DataList :dataSource="dataSource" @loadData="loadDataList">
      <template #default="{ data }">
        <div class="data-item">
          <Avatar
            :avatar="data.otherAvatar"
            :userId="data.otherUserId"
          ></Avatar>
          <div class="user-info">
            <div class="nick-name">
              <router-link
                :to="`/user/${data.otherUserId}`"
                target="_blank"
                class="a-link"
              >
                {{ data.otherNickName }}
              </router-link>
            </div>
            <div class="introduction">
              {{ data.otherPersonIntroduction || "这个人没有填简介啊~~~" }}
            </div>
          </div>
          <div class="op-btns">
            <div v-if="data.focusType == 1" class="focus-eachother">已互粉</div>
            <el-button
              link
              type="primary"
              @click="cancelFocus(data.otherUserId)"
              v-if="route.name == 'uhomeFocus' || data.focusType == 1"
              >取消关注</el-button
            >

            <el-button
              type="primary"
              v-if="route.name == 'uhomeFans' && data.focusType == 0"
              @click="focus(data.otherUserId)"
              >关注</el-button
            >
          </div>
        </div>
      </template>
    </DataList>
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  watch,
  inject,
} from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const dataSource = ref({});
const loadDataList = async (pCategoryCode, categoryCode) => {
  let params = {
    pageNo: dataSource.value.pageNo,
    pageSize: dataSource.value.pageSize,
  };
  let result = await proxy.Request({
    url:
      route.name == "uhomeFocus"
        ? proxy.Api.uHomeFocusList
        : proxy.Api.uHomeFansList,
    params,
  });
  if (!result) {
    return;
  }
  dataSource.value = result.data;
};

const cancelFocusUser = inject("cancelFocusUser");
const cancelFocus = (otherUserId) => {
  cancelFocusUser(otherUserId, () => {
    loadDataList();
  });
};

const focusUser = inject("focusUser");
const focus = (otherUserId) => {
  focusUser(otherUserId, () => {
    loadDataList();
  });
};

watch(
  () => route.name,
  (newVal, oldVal) => {
    console.log(newVal);
    if (newVal == "uhomeFocus" || newVal == "uhomeFans") {
      loadDataList();
    }
  },
  { immediate: true, deep: true }
);
</script>

<style lang="scss" scoped>
.body-container {
  background: #ffff;
  padding: 20px;
  border-radius: 5px;
  .body-title {
    font-size: 18px;
    color: #6d757a;
    border-bottom: 1px solid #e5e9ef;
    padding: 0px 0px 10px 0px;
  }
  .data-item {
    display: flex;
    align-items: center;
    padding: 10px;
    .user-info {
      flex: 1;
      margin: 0px 10px;
      .introduction {
        margin-top: 10px;
        font-size: 13px;
        color: #6d757a;
        overflow: hidden;
        white-space: nowrap;
        width: 100%;
        text-overflow: ellipsis;
        padding-right: 10px;
      }
    }
    .op-btns {
      display: flex;
      align-items: center;
      .focus-eachother {
        margin-right: 10px;
        color: var(--text3);
      }
    }
  }
}
</style>
