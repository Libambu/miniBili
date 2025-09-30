<template>
  <Dialog :show="dialogConfig.show" :title="dialogConfig.title" :buttons="dialogConfig.buttons" :showCancel="true"
    @close="dialogConfig.show = false">
    <el-form :model="formData" :rules="rules" ref="formDataRef" label-width="80px" @submit.prevent>
      <el-form-item label="分类编号" prop="categoryCode">
        <el-input :maxLength="10" v-model="formData.categoryCode" :show-word-limit="true" :maxlength="30" />
      </el-form-item>
      <el-form-item label="分类名称" prop="categoryName">
        <el-input :maxLength="10" v-model="formData.categoryName" :show-word-limit="true" :maxlength="30" />
      </el-form-item>
      <template v-if="formData.pCategoryId === 0">
        <el-form-item label="图标" prop="icon">
          <ImageUpload v-model="formData.icon"></ImageUpload>
        </el-form-item>
        <el-form-item label="背景图" prop="icon">
          <ImageUpload v-model="formData.background" :width="300" :height="150"></ImageUpload>
        </el-form-item>
      </template>
    </el-form>
  </Dialog>
</template>
<script setup>
import ImageUpload from '@/components/ImageUpload.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
import { uploadImage } from '@/utils/Api.js'
const dialogConfig = ref({
  show: false,
  title: '新增分类',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        submitForm()
      },
    },
  ],
})

const formData = ref({})
const formDataRef = ref()

const rules = {
  categoryCode: [{ required: true, message: '请输入分类编号' }],
  categoryName: [{ required: true, message: '请输入分类名称' }],
}

const showEdit = (data) => {
  dialogConfig.value.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    if (data.categoryId == null) {
      dialogConfig.value.title = '新增分类'
    } else {
      dialogConfig.value.title = '修改分类'
    }
    formData.value = Object.assign({}, data)
  })
}

defineExpose({
  showEdit,
})

const emit = defineEmits(['reload'])
const submitForm = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)

    //上传封面
    if (params.icon instanceof File) {
      params.icon = await uploadImage(params.icon)
    }
    //上传背景图
    if (params.background instanceof File) {
      params.background = await uploadImage(params.background)
    }

    delete params.children

    let result = await proxy.Request({
      url: proxy.Api.saveCategory,
      params,
    })
    if (!result) {
      return
    }
    dialogConfig.value.show = false
    proxy.Message.success('保存成功')
    emit('reload')
  })
}
</script>

<style lang="scss" scoped>
</style>
