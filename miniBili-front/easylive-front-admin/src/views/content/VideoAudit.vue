<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    width="600px"
    :showCancel="true"
    @close="dialogConfig.show = false"
  >
    <el-form
      :model="formData"
      :rules="rules"
      ref="formDataRef"
      label-width="80px"
      @submit.prevent
    >
      <!--input输入-->
      <el-form-item label="审核结果" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="3">审核通过</el-radio>
          <el-radio :value="4">审核不通过</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--input输入-->
      <el-form-item label="拒绝理由" prop="reason" v-if="formData.status == 4">
        <el-input
          resize="none"
          type="textarea"
          :rows="4"
          clearable
          placeholder="请输入拒绝理由"
          v-model="formData.reason"
          show-word-limit
          :maxlength="200"
        ></el-input>
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const formData = ref({});
const formDataRef = ref();
const rules = {
  status: [{ required: true, message: "请选择审核结果" }],
  reason: [{ required: true, message: "请输入拒绝理由" }],
};

const dialogConfig = ref({
  show: false,
  title: "审核",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        audit();
      },
    },
  ],
});

const show = (videoId) => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = {
      videoId,
    };
  });
};
defineExpose({
  show,
});

const emit = defineEmits(["reload"]);
const audit = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: proxy.Api.auditVideo,
      params,
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("审核成功");
    emit("reload");
  });
};
</script>

<style lang="scss" scoped>
</style>
