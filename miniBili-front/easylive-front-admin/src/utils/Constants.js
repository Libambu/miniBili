const ACTION_TYPE = {
    COMMENT_LIKE: {
        value: 0,
        desc: "评论喜欢点赞"
    },
    COMMENT_HATE: {
        value: 1,
        desc: "评论讨厌"
    },
    VIDEO_LIKE: {
        value: 2,
        desc: "视频点赞"
    },
    VIDEO_COLLECT: {
        value: 3,
        desc: "视频收藏"
    },
    VIDEO_COIN: {
        value: 4,
        desc: "视频投币"
    }
}

export {
    ACTION_TYPE
}