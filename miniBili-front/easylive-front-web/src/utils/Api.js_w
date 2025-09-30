import Request from "./Request";
//微服务版本
const server_web = "/web";
const server_file = "/file";
const server_interact = "/interact";

const Api = {
    //web
    checkCode: server_web + "/account/checkCode",
    login: server_web + "/account/login",
    logout: server_web + "/account/logout",
    register: server_web + "/account/register",
    autoLogin: server_web + "/account/autoLogin",
	getUserCountInfo: server_web + "/account/getUserCountInfo",
    loadAllCategory: server_web + "/category/loadAllCategory",
    getSysSetting: server_web + "/sysSetting/getSetting",
    getSearchKeywordTop: server_web + "/video/getSearchKeywordTop",
    loadRecommendVideo: server_web + "/video/loadRecommendVideo",
    loadVideo: server_web + "/video/loadVideo",

    postVideo: server_web + "/ucenter/postVideo",
    saveVideoInteraction: server_web + "/ucenter/saveVideoInteraction",
    getVideoByVideoId: server_web + "/ucenter/getVideoByVideoId",
    loadUcenterVideoList: server_web + "/ucenter/loadVideoList",
    getUcenterVideoCountInfo: server_web + "/ucenter/getVideoCountInfo",

    //个人中心
    ucLoadAllVideo: server_web + "/ucenter/loadAllVideo",

    ucGetActualTimeStatisticsInfo: server_web + "/ucenter/getActualTimeStatisticsInfo",
    getWeekStatisticsInfo: server_web + "/ucenter/getWeekStatisticsInfo",
    ucDeleteVideo: server_web + "/ucenter/deleteVideo",
    loadVideoPList: server_web + "/video/loadVideoPList",
    getVideoInfo: server_web + "/video/getVideoInfo",

    //个人主页
    uHomeUpdateUserInfo: server_web + "/uhome/updateUserInfo",
    uHomeLoadVideo: server_web + "/uhome/loadVideoList",
    uHomeGetUsesrInfo: server_web + "/uhome/getUserInfo",
    //关注
    uHomeFocus: server_web + "/uhome/focus",
    //取消关注
    uHomeCancelFocus: server_web + "/uhome/cancelFocus",
    //关注列表
    uHomeFocusList: server_web + "/uhome/loadFocusList",
    //粉丝列表
    uHomeFansList: server_web + "/uhome/loadFansList",
    //视频系列
    uHomeSeriesLoadVideoSeries: server_web + "/uhome/series/loadVideoSeries",
    //获取系列视频
    uHomeSeriesLoadAllVideo: server_web + "/uhome/series/loadAllVideo",
    //保存系列
    uHomeSeriesSaveVideoSeries: server_web + "/uhome/series/saveVideoSeries",
    //修改系列顺序
    uHomeSeriesChangeVideoSeriesSort: server_web + "/uhome/series/changeVideoSeriesSort",
    //获取系列详情
    uHomeSeriesGetVideoSeriesDetail: server_web + "/uhome/series/getVideoSeriesDetail",
    //删除系列
    uHomeSeriesDelVideoSeries: server_web + "/uhome/series/delVideoSeries",
    //保存系列视频
    uHomeSeriesSaveSeriesVideo: server_web + "/uhome/series/saveSeriesVideo",
    //删除系列视频
    uHomeSeriesDelSeriesVideo: server_web + "/uhome/series/delSeriesVideo",
    //获取所有列表
    uHomeSeriesLoadVideoSeriesWithVideo: server_web + "/uhome/series/loadVideoSeriesWithVideo",

    //设置主题
    saveTheme: server_web + "/uhome/saveTheme",
    //搜索
    search: server_web + "/video/search",
    //推荐视频
    getVideoRecommend: server_web + "/video/getVideoRecommend",
    //热门视频
    hotVideoList: server_web + "/video/loadHotVideoList",

    //播放历史
    playHisotry: server_web + "/history/loadHistory",
    delHistory: server_web + "/history/delHistory",
    cleanHistory: server_web + "/history/cleanHistory",

    //文件
    sourcePath: "/api" + server_file + "/getResource?sourceName=",
    getVideoResource: "/api" + server_file + "/videoResource",
    uploadImage: server_file + "/uploadImage",
    preUploadVideo: server_file + "/preUploadVideo",
    uploadVideo: server_file + "/uploadVideo",
    delUploadVideo: server_file + "/delUploadVideo",

    //互动
    loadComment: server_interact + "/comment/loadComment",
    postComment: server_interact + "/comment/postComment",
    userDelComment: server_interact + "/comment/userDelComment",
    userTopComment: server_interact + "/comment/topComment",
    userCancelTopComment: server_interact + "/comment/cancelTopComment",
    //弹幕
    loadDanmu: server_interact + "/danmu/loadDanmu",
    postDanmu: server_interact + "/danmu/postDanmu",
    //消息
    getNoReadCount: server_interact + "/message/getNoReadCount",
    loadUserMessage: server_interact + "/message/loadMessage",
    delMessage: server_interact + "/message/delMessage",
    getNoReadCountGroup: server_interact + "/message/getNoReadCountGroup",
    readAll: server_interact + "/message/readAll",
    //上报在线人数
    reportVideoPlayOnline: server_interact + "/online/reportVideoPlayOnline",
    //点赞，评论，投币，评论，收藏
    userAction: server_interact + "/userAction/doAction",

    //用户中心，主页 相关接口移到 互动模块
    uHomeLoadCollection: server_interact + "/uhome/loadUserCollection",
    ucLoadComment: server_interact + "/ucenter/loadComment",
    ucDelComment: server_interact + "/ucenter/delComment",
    ucLoadDanmu: server_interact + "/ucenter/loadDanmu",
    ucDelDanmu: server_interact + "/ucenter/delDanmu",
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