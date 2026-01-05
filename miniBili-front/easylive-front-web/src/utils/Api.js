import Request from "./Request";
//单服务版本
const Api = {
    checkCode: "/account/checkCode",
    login: "/account/login",
    logout: "/account/logout",
    register: "/account/register",
    autoLogin: "/account/autoLogin",
    getUserCountInfo: "/account/getUserCountInfo",
    sourcePath: "/api/file/getResource?sourceName=",
    loadAllCategory: "/category/loadAllCategory",
    getSysSetting: "/sysSetting/getSetting",
    //发布视频
    preUploadVideo: "/file/preUploadVideo",
    uploadVideo: "/file/uploadVideo",
    delUploadVideo: "/file/delUploadVideo",
    postVideo: "/ucenter/postVideo",
    saveVideoInteraction: "/ucenter/saveVideoInteraction",
    getVideoByVideoId: "/ucenter/getVideoByVideoId",
    loadUcenterVideoList: "/ucenter/loadVideoList",
    getUcenterVideoCountInfo: "/ucenter/getVideoCountInfo",
    uploadImage: "/file/uploadImage",
    //个人中心
    ucLoadAllVideo: "/ucenter/loadAllVideo",
    ucLoadComment: "/ucenter/loadComment",
    ucDelComment: "/ucenter/delComment",
    ucLoadDanmu: "/ucenter/loadDanmu",
    ucDelDanmu: "/ucenter/delDanmu",
    ucGetActualTimeStatisticsInfo: "/ucenter/getActualTimeStatisticsInfo",
    getWeekStatisticsInfo: "/ucenter/getWeekStatisticsInfo",
    ucDeleteVideo: "/ucenter/deleteVideo",
    //获取视频列表
    loadRecommendVideo: "/video/loadRecommendVideo",
    loadVideo: "/video/loadVideo",
    loadVideoPList: "/video/loadVideoPList",
    getVideoResource: "/api/file/videoResource",
    getVideoInfo: "/video/getVideoInfo",
    //评论
    loadComment: "/comment/loadComment",
    postComment: "/comment/postComment",
    userDelComment: "/comment/userDelComment",
    userTopComment: "/comment/topComment",
    userCancelTopComment: "/comment/cancelTopComment",
    //弹幕
    loadDanmu: "/danmu/loadDanmu",
    postDanmu: "/danmu/postDanmu",
    //上报在线人数
    reportVideoPlayOnline: "/video/reportVideoPlayOnline",
    //点赞，评论，投币，评论，收藏
    userAction: "/userAction/doAction",
    //播放历史
    playHisotry: "/history/loadHistory",
    delHistory: "/history/delHistory",
    cleanHistory: "/history/cleanHistory",
    //消息
    getNoReadCount: "/message/getNoReadCount",
    loadUserMessage: "/message/loadMessage",
    delMessage: "/message/delMessage",
    getNoReadCountGroup: "/message/getNoReadCountGroup",
    readAll: "/message/readAll",
    //个人主页
    uHomeUpdateUserInfo: "/uhome/updateUserInfo",
    uHomeLoadVideo: "/uhome/loadVideoList",
    uHomeGetUsesrInfo: "/uhome/getUserInfo",
    //关注
    uHomeFocus: "/uhome/focus",
    //取消关注
    uHomeCancelFocus: "/uhome/cancelFocus",
    //关注列表
    uHomeFocusList: "/uhome/loadFocusList",
    //粉丝列表
    uHomeFansList: "/uhome/loadFansList",
    //视频系列
    uHomeSeriesLoadVideoSeries: "/uhome/series/loadVideoSeries",
    //获取系列视频
    uHomeSeriesLoadAllVideo: "/uhome/series/loadAllVideo",
    //保存系列
    uHomeSeriesSaveVideoSeries: "/uhome/series/saveVideoSeries",
    //修改系列顺序
    uHomeSeriesChangeVideoSeriesSort: "/uhome/series/changeVideoSeriesSort",
    //获取系列详情
    uHomeSeriesGetVideoSeriesDetail: "/uhome/series/getVideoSeriesDetail",
    //删除系列
    uHomeSeriesDelVideoSeries: "/uhome/series/delVideoSeries",
    //保存系列视频
    uHomeSeriesSaveSeriesVideo: "/uhome/series/saveSeriesVideo",
    //删除系列视频
    uHomeSeriesDelSeriesVideo: "/uhome/series/delSeriesVideo",
    //获取所有列表
    uHomeSeriesLoadVideoSeriesWithVideo: "/uhome/series/loadVideoSeriesWithVideo",
    //收藏列表
    uHomeLoadCollection: "/uhome/loadUserCollection",
    //设置主题
    saveTheme: "/uhome/saveTheme",
    //搜索
    search: "/video/search",
    getSearchKeywordTop: "/video/getSearchKeywordTop",
    //推荐视频
    getVideoRecommend: "/video/getVideoRecommend",
    //热门视频
    hotVideoList: "/video/loadHotVideoList"
}

//上传封面
const uploadImage = async (file, createThumbnail = false) => {
    let result = await Request({
        url: Api.uploadImage,
        params: {
            file,
            createThumbnail
        },
    })
    if (!result) {
        return;
    }
    return result.data;
}

const doUserAction = async (config, callback) => {
    let result = await Request({
        url: Api.userAction,
        params: config,
        showLoading: true,
    })
    if (!result) {
        return;
    }
    callback()
}

export {
    Api,
    uploadImage,
    doUserAction
}