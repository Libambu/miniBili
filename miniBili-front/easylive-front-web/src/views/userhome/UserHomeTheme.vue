<template>
  <div class="theme-body">
    <el-drawer
      lock-scroll
      v-model="showDrawer"
      title="选择背景"
      direction="btt"
      size="45%"
    >
      <el-scrollbar height="100%">
        <div class="theme-gird-list">
          <div
            class="grid-item"
            v-for="item in themeCount"
            @click="selectTheme(item)"
          >
            <img
              :src="proxy.Utils.getLocalImage('uhome-bg/' + item + '.png')"
            />
            <div
              class="checked iconfont icon-checked"
              v-if="currentTheme == item"
            ></div>
          </div>
        </div>
      </el-scrollbar>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { useRouter } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();

const showDrawer = ref(false);

const themeCount = 10;
const currentTheme = ref(0);

const emit = defineEmits(["changeTheme"]);
const selectTheme = async (item) => {
  currentTheme.value = item;
  let result = await proxy.Request({
    url: proxy.Api.saveTheme,
    params: {
      theme: item,
    },
  });
  if (!result) {
    return;
  }
  showDrawer.value = false;
  emit("changeTheme", item);
};

const show = (theme) => {
  showDrawer.value = true;
  currentTheme.value = theme;
};
defineExpose({
  show,
});
</script>

<style lang="scss" scoped>
.theme-body {
  :deep(.el-drawer__body) {
    padding: 0px;
  }
  :deep(.el-drawer__header) {
    margin-bottom: 0px;
  }
  .theme-gird-list {
    margin: 0px auto;
    width: 1400px;
    display: grid;
    grid-gap: 20px;
    grid-template-columns: repeat(5, 1fr);
    .grid-item {
      position: relative;
      cursor: pointer;
      .checked {
        position: absolute;
        top: 0px;
        right: 0px;
        color: var(--blue);
        font-size: 30px;
      }

      img {
        width: 100%;
        object-fit: fill;
        border: 2px solid #ddd;
        &:hover {
          border: 2px solid var(--blue);
        }
      }
    }
  }
}
</style>
