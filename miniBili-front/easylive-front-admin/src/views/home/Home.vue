<template>
  <el-card class="box-card">
    <div slot="header">
      <div class="part-title">视频数据</div>
    </div>
    <div class="video-data-list">
      <div :class="[
          'video-data-item',
          item.preDataType == currentDataPart.preDataType ? 'active' : '',
        ]" v-for="item in dataPartList" @click="loadWeekDataHandler(item)">
        <div class="video-data-title">
          <div :class="['name iconfont', item.icon]">{{ item.name }}</div>
          <div class="pre-count">{{ item.preCount }}</div>
        </div>
        <div class="total-count">{{ item.totalCount }}</div>
      </div>
    </div>
  </el-card>

  <el-card class="week-data-panel">
    <div ref="chartRef" class="data-chart"></div>
  </el-card>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, shallowRef } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()
import * as echarts from 'echarts'

const dataPartList = ref([
  {
    name: '用户数',
    icon: 'icon-user',
    totalCountKey: 'userCount',
    preDataType: 1,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '播放',
    icon: 'icon-play-solid',
    totalCountKey: 'playCount',
    preDataType: 0,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '评论',
    icon: 'icon-comment-solid',
    totalCountKey: 'commentCount',
    preDataType: 5,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '弹幕',
    icon: 'icon-danmu-solid',
    totalCountKey: 'danmuCount',
    preDataType: 6,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '点赞',
    icon: 'icon-like-solid',
    totalCountKey: 'likeCount',
    preDataType: 2,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '收藏',
    icon: 'icon-collection-solid',
    totalCountKey: 'collectCount',
    preDataType: 3,
    totalCount: 0,
    preCount: 0,
  },
  {
    name: '投币',
    icon: 'icon-toubi',
    totalCountKey: 'coinCount',
    preDataType: 4,
    totalCount: 0,
    preCount: 0,
  },
])

const getActualTimeStatisticsInfo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getActualTimeStatisticsInfo,
  })
  if (!result) {
    return
  }
  const totalCountInfo = result.data.totalCountInfo
  const preDayData = result.data.preDayData

  dataPartList.value.forEach((item) => {
    item.totalCount = totalCountInfo[item.totalCountKey]
    item.preCount = preDayData[item.preDataType]
      ? preDayData[item.preDataType]
      : 0
  })
}
getActualTimeStatisticsInfo()

const chartRef = ref(null)
const dataChart = shallowRef()
const init = () => {
  nextTick(() => {
    dataChart.value = echarts.init(chartRef.value)
    loadWeekData()
  })
}
init()

const loadWeekDataHandler = (item) => {
  currentDataPart.value = item
  loadWeekData()
}

const currentDataPart = ref(dataPartList.value[0])
const loadWeekData = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getWeekStatisticsInfo,
    params: {
      dataType: currentDataPart.value.preDataType,
    },
  })
  if (!result) {
    return
  }
  const dateArray = []
  const dataCountArray = []
  result.data.forEach((item) => {
    dateArray.push(item.statisticsDate)
    dataCountArray.push(item.statisticsCount)
  })

  const option = {
    title: {
      text: `近7天${currentDataPart.value.name}量`,
    },
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: [currentDataPart.value.name],
    },
    toolbox: {
      show: true,
      feature: {
        mark: { show: true },
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ['line', 'bar'] },
        restore: { show: true },
        saveAsImage: { show: true },
      },
    },
    calculable: true,
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: dateArray,
      },
    ],
    yAxis: [
      {
        type: 'value',
        axisLabel: {
          formatter: '{value} °C',
        },
      },
    ],
    series: [
      {
        name: currentDataPart.value.name,
        type: 'line',
        data: dataCountArray,
        smooth: true,
        itemStyle: {
          normal: {
            color: '#ff6699', // 曲线颜色
            lineStyle: {
              color: '#ff6699', // 点的边框颜色
            },
          },
        },
      },
    ],
  }
  dataChart.value.setOption(option, true)
}
</script>

<style lang="scss" scoped>
.part-title {
  font-size: 18px;
  margin-bottom: 20px;
}
.video-data-list {
  display: grid;
  grid-gap: 20px;
  grid-template-columns: repeat(4, 1fr);
  padding-bottom: 10px;
  .video-data-item {
    cursor: pointer;
    background: #f5fcfe;
    padding: 20px;
    border-radius: 5px;
    .video-data-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .iconfont {
        color: var(--text3);
        font-size: 14px;
        &::before {
          font-size: 20px;
          margin-right: 5px;
          float: left;
        }
      }
      .pre-count {
        color: #ff4684;
      }
    }
    .total-count {
      margin-top: 10px;
      font-weight: bold;
      color: var(--blue);
      font-size: 20px;
    }
  }
  .active {
    background: #ff4684;
    .video-data-title {
      .iconfont {
        color: #fff;
      }
      .pre-count {
        color: #fff;
      }
    }
    .total-count {
      color: #fff;
    }
  }
}

.data-chart {
  height: 400px;
}
.week-data-panel {
  margin-top: 10px;
}
</style>
