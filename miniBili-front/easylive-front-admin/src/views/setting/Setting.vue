<template>
  <div class="setting-form">
    <el-form
      :model="formData"
      :rules="rules"
      ref="formDataRef"
      label-width="160px"
      @submit.prevent
    >
      <!--input输入-->
      <el-form-item label="注册送硬币数量" prop="registerCoinCount">
        <el-input
          placeholder="请输入硬币数量"
          v-model="formData.registerCoinCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="发布视频送硬币数量" prop="postVideoCoinCount">
        <el-input
          placeholder="请输入发布视频送硬币数量"
          v-model="formData.postVideoCoinCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="单个视频大小" prop="videoSize">
        <el-input
          placeholder="单个视频大小"
          v-model="formData.videoSize"
          type="number"
          :min="1"
        >
          <template #suffix> MB </template>
        </el-input>
      </el-form-item>
      <el-form-item label="稿件最大分P数量" prop="videoPCount">
        <el-input
          placeholder="单个视频大小"
          v-model="formData.videoPCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="每天发布视频数" prop="videoCount">
        <el-input
          placeholder="每天发布视频数"
          v-model="formData.videoCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="每天允许评论数" prop="commentCount">
        <el-input
          placeholder="每天允许发布评论数"
          v-model="formData.commentCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="每天允许弹幕数" prop="danmuCount">
        <el-input
          placeholder="每天允许发布弹幕数"
          v-model="formData.danmuCount"
          type="number"
          :min="1"
        >
        </el-input>
      </el-form-item>
      <!--input输入-->
      <el-form-item label="" prop="">
        <el-button type="primary" @click="saveSetting">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { useRouter } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();

const formData = ref({});
const formDataRef = ref();
const rules = {
  title: [{ required: true, message: "请输入内容" }],
  videoCount: [{ required: true, message: "请输入视频数量" }],
  videoPCount: [{ required: true, message: "请输入稿件最大分P数量" }],
  commentCount: [{ required: true, message: "请输入评论数量" }],
  danmuCount: [{ required: true, message: "请输入弹幕数量" }],
};

const getSetting = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getSetting,
  });
  if (!result) {
    return;
  }
  formData.value = result.data;
};
getSetting();

const saveSetting = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: proxy.Api.saveSetting,
      params,
    });
    if (!result) {
      return;
    }
    proxy.Message.success("保存成功");
  });
};
</script>

<style lang="scss" scoped>
.setting-form {
  padding: 20px;
  width: 500px;
}
</style>
