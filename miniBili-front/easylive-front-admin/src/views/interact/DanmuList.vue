<template>
  <div class="top-panel">
    <el-card>
      <el-form :model="searchForm" @submit.prevent>
        <el-row :gutter="10">
          <el-col :span="5">
            <!--input输入-->
            <el-form-item label="视频">
              <el-input clearable placeholder="输入视频名称搜索" v-model="searchForm.videoNameFuzzy"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-button type="primary" @click="loadDataList">搜索</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
  <el-card class="table-data-card">
    <Table ref="tableInfoRef" :columns="columns" :fetch="loadDataList" :dataSource="tableData" :options="tableOptions"
      :extHeight="tableOptions.extHeight">

      <template #slotNickName="{index,row}">
        <a target="_blank" class="nick-name" :href="`${proxy.webDomain}/user/${row.userId}`">{{row.nickName}}</a>
      </template>
      <template #time="{ index, row }">
        {{proxy.Utils.convertSecondsToHMS(Math.round(row.time))}}
      </template>

      <template #slotOperation="{ index, row }">
        <a href="javascript:void(0)" class="a-link" @click="delDanmu(row.danmuId)">删除</a>
      </template>

      <template #slotText="{index,row}">
        <div>{{row.text}}</div>
        <a target="_blank" class="video-info" :href="`${proxy.webDomain}/video/${row.videoId}`">视频：{{row.videoName}}</a>
      </template>
    </Table>
  </el-card>
</template>

<script setup>
import Table from '@/components/Table.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
import { useRouter } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()

const columns = [
  {
    label: '发送者',
    prop: 'nickName',
    width: 150,
    scopedSlots: 'slotNickName',
  },
  {
    label: '播放时间',
    prop: 'time',
    scopedSlots: 'time',
    width: 100,
  },
  {
    label: '弹幕内容',
    prop: 'text',
    scopedSlots: 'slotText',
  },
  {
    label: '发送时间',
    prop: 'postTime',
    width: 180,
  },
  {
    label: '操作',
    prop: 'operation',
    width: 80,
    scopedSlots: 'slotOperation',
  },
]

const tableInfoRef = ref()
const tableOptions = ref({
  extHeight: 0,
})

const searchForm = ref({})
const tableData = ref({})
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  }
  Object.assign(params, searchForm.value)
  let result = await proxy.Request({
    url: proxy.Api.loadDanmu,
    params: params,
  })
  if (!result) {
    return
  }
  Object.assign(tableData.value, result.data)
}

const delDanmu = (danmuId) => {
  proxy.Confirm({
    message: '确定要删除吗？',
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.delDanmu,
        params: {
          danmuId,
        },
      })
      if (!result) {
        return
      }
      proxy.Message.success('删除成功')
      loadDataList()
    },
  })
}
</script>

<style lang="scss" scoped>
.video-info,
.nick-name {
  margin-top: 5px;
  font-size: 12px;
  color: var(--text3);
  text-decoration: none;
}
.nick-name {
  font-size: 14px;
  color: var(--text2);
}
</style>
