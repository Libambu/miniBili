import Request from "./Request";

const Api = {
    sourcePath: "/api/file/getResource?sourceName=",
    checkCode: "/account/checkCode",
    login: "/account/login",
    uploadImage: "/file/uploadImage",
    //分类
    loadCategory: "/category/loadCategory",
    saveCategory: "/category/saveCategory",
    delCategory: "/category/delCategory",
    changeCategorySort: "/category/changeSort",
    //视频
    loadVideo: "/videoInfo/loadVideoList",
    loadVideoPList: "/videoInfo/loadVideoPList",
    //推荐视频
    recommendVideo: "/videoInfo/recommendVideo",
    //互动
    loadComment: "/interact/loadComment",
    delComment: "/interact/delComment",
    //弹幕
    loadDanmu: "/interact/loadDanmu",
    delDanmu: "/interact/delDanmu",
    //用户管理
    loadUser: "/user/loadUser",
    changeStatus: "/user/changeStatus",
    //视频资源
    getVideoResource: "/api/file/videoResource",
    auditVideo: "/videoInfo/auditVideo",
    deleteVideo: "/videoInfo/deleteVideo",
    getSetting: "/setting/getSetting",
    saveSetting: "/setting/saveSetting",
    //首页统计数据
    getActualTimeStatisticsInfo: "/index/getActualTimeStatisticsInfo",
    getWeekStatisticsInfo: "/index/getWeekStatisticsInfo"
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


export {
    Api,
    uploadImage,
}