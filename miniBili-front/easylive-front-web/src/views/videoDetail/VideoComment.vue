<template>
  <div class="comment-panel">
    <div class="comment-title">
      <div class="title">
        评论<span class="comment-count">{{ dataSource.totalCount }}</span>
      </div>
      <div
          :class="['order-type-item', orderType == 0 ? 'active' : '']"
          @click="changeOrder(0)"
      >
        最热
      </div>
      <el-divider direction="vertical"/>
      <div
          :class="['order-type-item', orderType == 1 ? 'active' : '']"
          @click="changeOrder(1)"
      >
        最新
      </div>
    </div>
    <div class="comment-content-panel">
      <VideoCommentSend
          :sendType="0"
          :showSend="showComment"
      ></VideoCommentSend>
      <div class="comment-list">
        <DataLoadMoreList
            :dataSource="dataSource"
            :loading="loadingData"
            @loadData="loadCommentList"
            layoutType="list"
            loadEndMsg="没有更多评论"
        >
          <template #default="{ data }">
            <VideoCommentItem :data="data"></VideoCommentItem>
          </template>
        </DataLoadMoreList>
      </div>
    </div>
  </div>
</template>

<script setup>
import {mitter} from "@/eventbus/eventBus.js";
import {ACTION_TYPE} from "@/utils/Constants.js";
import VideoCommentItem from "./VideoCommentItem.vue";
import VideoCommentSend from "./VideoCommentSend.vue";
import {computed, getCurrentInstance, inject, onMounted, onUnmounted, provide, ref,} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useLoginStore} from "@/stores/loginStore";

const {proxy} = getCurrentInstance();
const route = useRoute();
const router = useRouter();
const loginStore = useLoginStore();

//判断是否显示弹幕
const videoInfo = inject("videoInfo");
const showComment = computed(() => {
  return (
      videoInfo.value.interaction == null ||
      videoInfo.value.interaction.indexOf("1") == -1
  );
});

const showReplyHandler = (commentId) => {
  dataSource.value.list.forEach((item) => {
    if (item.commentId != commentId) {
      item.showReply = false;
    } else {
      item.showReply = true;
    }
  });
};
provide("showReply", showReplyHandler);

const loadingData = ref(false);
const dataSource = ref({});
const orderType = ref(0);

const changeOrder = (_orderType) => {
  orderType.value = _orderType;
  loadCommentList();
};

const loadCommentList = async () => {
  if (!showComment.value) {
    return;
  }
  loadingData.value = true;
  let result = await proxy.Request({
    url: proxy.Api.loadComment,
    params: {
      videoId: route.params.videoId,
      pageNo: dataSource.value.pageNo,
      orderType: orderType.value,
    },
  });
  loadingData.value = false;
  if (!result) {
    return;
  }
  const userActionMap = {};
  const userActionList = result.data.userActionList;
  userActionList.forEach((item) => {
    userActionMap[item.commentId] = item;
  });
  const commentData = result.data.commentData;
  commentData.list.forEach((item) => {
    setCommentActive(userActionMap, item);
    if (item.children) {
      item.children.forEach((sub) => {
        setCommentActive(userActionMap, sub);
      });
    }
  });
  const dataList = dataSource.value.list;
  dataSource.value = Object.assign({}, commentData);
  if (commentData.pageNo > 1) {
    dataSource.value.list = dataList.concat(commentData.list);
  }
};
loadCommentList();

//设置已点赞
const setCommentActive = (userActionMap, item) => {
  const userActon = userActionMap[item.commentId];
  if (userActon) {
    if (ACTION_TYPE.COMMENT_LIKE.value == userActon.actionType) {
      item.likeCountActive = true;
    } else if (ACTION_TYPE.COMMENT_HATE.value == userActon.actionType) {
      item.hateCountActive = true;
    }
  }
};

//删除评论
const delCommentCallback = ({pCommentId, commentId}) => {
};

onMounted(() => {
  mitter.on("postCommentSuccess", (comment) => {
    if (comment.pCommentId === 0) {
      dataSource.value.list.unshift(comment);
      if (!dataSource.value.totalCount) {
        dataSource.value.totalCount = 1;
      } else {
        dataSource.value.totalCount++;
      }
      return;
    } else {
      //二级回复
      const curComment = dataSource.value.list.find((item) => {
        return item.commentId == comment.pCommentId;
      });
      if (!curComment) {
        return;
      }
      if (!curComment.children) {
        curComment.children = [];
      }
      curComment.children.push(comment);
    }
  });

  //删除评论
  mitter.on("delCommentCallback", ({pCommentId, commentId}) => {
    if (pCommentId == 0) {
      dataSource.value.list = dataSource.value.list.filter((item) => {
        return item.commentId != commentId;
      });
      dataSource.value.totalCount--;
    } else {
      const pComment = dataSource.value.list.find((item) => {
        return item.commentId == pCommentId;
      });
      pComment.children = pComment.children.filter((item) => {
        return item.commentId != commentId;
      });
    }
  });

  //置顶
  mitter.on("topCommentCallback", () => {
    loadCommentList();
  });
});

onUnmounted(() => {
  mitter.off("postCommentSuccess");
  mitter.off("delCommentCallback");
  mitter.off('topCommentCallback')
});
</script>

<style lang="scss" scoped>
.comment-panel {
  margin-top: 20px;

  .comment-title {
    display: flex;
    align-items: center;
    font-size: 15px;

    .title {
      font-size: 20px;
      font-weight: 500;

      .comment-count {
        margin-left: 5px;
        font-size: 14px;
        margin-right: 30px;
        color: var(--text2);
      }
    }

    .order-type-item {
      cursor: pointer;
    }

    .active {
      color: var(--blue);
    }
  }

  .comment-content-panel {
    padding-left: 10px;
    position: relative;

    .comment-list {
      padding-bottom: 20px;
    }
  }
}
</style>
