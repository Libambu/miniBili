create table branch_table
(
    branch_id         bigint        not null
        primary key,
    xid               varchar(128)  not null,
    transaction_id    bigint        null,
    resource_group_id varchar(32)   null,
    resource_id       varchar(256)  null,
    branch_type       varchar(8)    null,
    status            tinyint       null,
    client_id         varchar(64)   null,
    application_data  varchar(2000) null,
    gmt_create        datetime(6)   null,
    gmt_modified      datetime(6)   null
)
    charset = utf8mb4;

create index idx_xid
    on branch_table (xid);

create table category_info
(
    category_id   int auto_increment comment '自增分类id'
        primary key,
    category_code varchar(30) not null comment '分类编码',
    category_name varchar(30) not null comment '分类名称',
    p_category_id int         not null comment '父级分类id',
    icon          varchar(50) null comment '图标',
    background    varchar(50) null comment '背景图',
    sort          tinyint     null comment '排序号',
    constraint category_info_pk_2
        unique (category_code)
)
    comment '分类信息表';

create table distributed_lock
(
    lock_key   char(20)    not null
        primary key,
    lock_value varchar(20) not null,
    expire     bigint      null
)
    charset = utf8mb4;

create table global_table
(
    xid                       varchar(128)  not null
        primary key,
    transaction_id            bigint        null,
    status                    tinyint       not null,
    application_id            varchar(32)   null,
    transaction_service_group varchar(32)   null,
    transaction_name          varchar(128)  null,
    timeout                   int           null,
    begin_time                bigint        null,
    application_data          varchar(2000) null,
    gmt_create                datetime      null,
    gmt_modified              datetime      null
)
    charset = utf8mb4;

create index idx_status_gmt_modified
    on global_table (status, gmt_modified);

create index idx_transaction_id
    on global_table (transaction_id);

create table lock_table
(
    row_key        varchar(128)      not null
        primary key,
    xid            varchar(128)      null,
    transaction_id bigint            null,
    branch_id      bigint            not null,
    resource_id    varchar(256)      null,
    table_name     varchar(32)       null,
    pk             varchar(36)       null,
    status         tinyint default 0 not null comment '0:locked ,1:rollbacking',
    gmt_create     datetime          null,
    gmt_modified   datetime          null
)
    charset = utf8mb4;

create index idx_branch_id
    on lock_table (branch_id);

create index idx_status
    on lock_table (status);

create index idx_xid
    on lock_table (xid);

create table statistics_info
(
    statistics_date  datetime    not null comment '统计日期',
    user_id          varchar(10) not null,
    data_type        tinyint(1)  not null comment '数据统计类型',
    statistics_count int         null comment '统计数量',
    primary key (statistics_date, user_id, data_type)
);

create table undo_log
(
    id            bigint auto_increment
        primary key,
    branch_id     bigint       not null,
    xid           varchar(100) not null,
    context       varchar(128) not null,
    rollback_info longblob     not null,
    log_status    int          not null,
    log_created   datetime     not null,
    log_modified  datetime     not null,
    ext           varchar(100) null,
    constraint ux_undo_log
        unique (xid, branch_id)
)
    charset = utf8mb3;

create table user_action
(
    action_id     int auto_increment comment '自增id'
        primary key,
    video_id      varchar(10)   not null comment '视频ID',
    video_user_id varchar(10)   not null comment '视频用户ID',
    comment_id    int default 0 not null comment '评论ID',
    action_type   tinyint(1)    not null comment '0评论喜欢点赞 1讨厌评论 2视频点赞 3视频收藏 4视频投币',
    action_count  int           not null comment '数量',
    user_id       varchar(10)   not null comment '用户ID',
    action_time   datetime      not null comment '操作时间',
    constraint idx_key_video_comment_type_user
        unique (video_id, comment_id, action_id, user_id)
)
    comment '用户行为，点赞、评论';

create index idx_action_id
    on user_action (action_time);

create index idx_type
    on user_action (action_type);

create index idx_user_id
    on user_action (user_id);

create index idx_video_id
    on user_action (video_id);

create table user_focus
(
    user_id       varchar(10) not null comment '用户id',
    focus_user_id varchar(10) not null comment '用户id',
    focus_time    datetime    null,
    primary key (user_id, focus_user_id)
);

create table user_info
(
    user_id             varchar(10)       not null comment '用户id'
        primary key,
    nick_name           varchar(20)       null comment '用户昵称',
    email               varchar(150)      null comment '用户邮箱',
    password            varchar(50)       not null comment '用户密码',
    sex                 tinyint           null comment '性别 0女1男2未知',
    birthday            varchar(10)       null,
    school              varchar(150)      null comment '学校',
    person_introduction varchar(200)      null comment '个人简介',
    join_time           datetime          not null comment '注册时间',
    last_login_time     datetime          null comment '最新登陆时间',
    status              int               not null comment '状态 0禁用 1正常',
    notice_info         varchar(300)      null comment '空间公告',
    total_coin_count    int               not null comment '总硬币数',
    current_coin_count  int               not null comment '当前硬币数',
    theme               tinyint default 1 not null comment '主题',
    last_login_ip       varchar(30)       null,
    avatar              varchar(50)       null,
    constraint email
        unique (email),
    constraint nick_name
        unique (nick_name),
    constraint user_id
        unique (user_id)
)
    comment '用户信息表';

create table user_message
(
    message_id   int auto_increment comment '消息ID自增'
        primary key,
    user_id      varchar(10) not null comment '用户id',
    video_id     varchar(10) null comment '视频id',
    message_type tinyint(1)  null comment '消息类型1系統消息2点赞3收藏4评论',
    send_user_id varchar(10) null comment '发送人id',
    read_type    tinyint(1)  null comment '0未读 1已读',
    create_time  datetime    null comment '创建时间',
    extend_json  text        null comment '扩展信息'
)
    comment '用户消息表';

create index user_message_message_type_index
    on user_message (message_type);

create index user_message_read_type_index
    on user_message (read_type);

create index user_message_user_id_index
    on user_message (user_id);

create table user_video_series
(
    series_id          int auto_increment comment '列表id'
        primary key,
    series_name        varchar(100) not null comment '列表名称',
    series_description varchar(200) null comment '描述',
    user_id            varchar(10)  not null,
    sort               tinyint      not null,
    update_time        datetime     null
);

create index user_video_series_user_id_index
    on user_video_series (user_id);

create table user_video_series_video
(
    series_id int         not null comment '列表id',
    video_id  varchar(10) not null,
    user_id   varchar(10) not null,
    sort      tinyint     not null,
    primary key (series_id, video_id)
);

create table video_comment
(
    comment_id    int auto_increment comment '评论id'
        primary key,
    p_comment_id  int               not null comment '父级评论id',
    video_id      varchar(10)       not null comment '视频id',
    video_user_id varchar(10)       not null comment '视频用户id',
    content       varchar(500)      null comment '回复内容',
    img_path      varchar(150)      null comment '图片',
    user_id       varchar(15)       not null comment '用户id',
    reply_user_id varchar(15)       null comment '回复人id',
    top_type      tinyint default 0 null comment '0未置顶1置顶',
    post_time     datetime          not null comment '发布时间',
    like_count    int     default 0 null comment '喜欢数量',
    hate_count    int     default 0 null comment '讨厌数量'
)
    comment '评论';

create index idx_p_id
    on video_comment (p_comment_id);

create index idx_post_time
    on video_comment (post_time);

create index idx_top
    on video_comment (top_type);

create index idx_user_id
    on video_comment (user_id);

create index idx_video_id
    on video_comment (video_id);

create table video_danmu
(
    danmu_id  int auto_increment comment '自增ID'
        primary key,
    video_id  varchar(20)  not null comment '视频id',
    file_id   varchar(20)  not null comment '唯一id',
    user_id   varchar(15)  not null comment '用户id',
    post_time datetime     null comment '发布时间',
    text      varchar(300) null comment '发布内容',
    mode      tinyint(1)   null comment '展示位置',
    color     varchar(10)  null comment '颜色',
    time      int          null comment '展示时间'
)
    comment '视频弹幕';

create table video_info
(
    video_id         varchar(10)          not null comment '视频id'
        primary key,
    video_cover      varchar(50)          not null comment '视频封面',
    video_name       varchar(100)         not null comment '视频名称',
    user_id          varchar(10)          not null comment '用户id',
    create_time      datetime             not null comment '创建时间',
    last_update_time datetime             not null comment '最后更新时间',
    p_category_id    int                  not null comment '父级分类id',
    category_id      int                  null comment '分类id',
    post_type        int                  not null comment '0自制作1转载',
    origin_info      varchar(200)         null comment '原资源说明',
    tags             varchar(300)         null comment '标签',
    introduction     varchar(200)         null comment '简介',
    interaction      varchar(5)           null comment '互动设置1关闭弹幕0关闭评论',
    duration         int        default 0 null comment '持续时间',
    play_count       int        default 0 null comment '播放量',
    like_count       int        default 0 null comment '点赞量',
    danmu_count      int        default 0 null comment '弹幕数量',
    comment_count    int        default 0 null comment '评论数量',
    coin_count       int        default 0 null comment '硬币数',
    collect_count    int        default 0 null comment '收藏数',
    recommend_type   tinyint(1) default 0 null comment '是否推荐 0未推荐1已推荐',
    last_play_time   datetime             null comment '最后播放时间'
)
    comment '视频信息表(审核后)';

create index video_info_category_id_index
    on video_info (category_id);

create index video_info_create_time_index
    on video_info (create_time);

create index video_info_last_update_time_index
    on video_info (last_update_time);

create index video_info_p_category_id_index
    on video_info (p_category_id);

create index video_info_recommend_type_index
    on video_info (recommend_type);

create index video_info_user_id_index
    on video_info (user_id);

create table video_info_file
(
    file_id    varchar(20)   not null comment '唯一id'
        primary key,
    user_id    varchar(20)   not null comment '用户id',
    video_id   varchar(10)   not null comment '视频id',
    file_name  varchar(200)  null comment '文件名称',
    file_index int           not null comment '文件索引',
    file_size  bigint        null comment '文件大小',
    file_path  varchar(100)  null comment '文件路径',
    duration   int default 0 null comment '持续时间'
);

create index video_info_file_video_id_index
    on video_info_file (video_id);

create table video_info_file_post
(
    file_id         varchar(20)   not null comment '唯一id'
        primary key,
    upload_id       varchar(15)   not null comment '上传id',
    user_id         varchar(20)   not null comment '用户id',
    video_id        varchar(10)   not null comment '视频id',
    file_index      int           not null comment '文件索引',
    file_name       varchar(200)  null comment '文件名称',
    file_size       bigint        null comment '文件大小',
    file_path       varchar(100)  null comment '文件路径',
    update_type     tinyint       null comment '0无更新1有更新',
    transfer_result tinyint       null comment '0转码中1转码成功2转码失败',
    duration        int default 0 null comment '持续时间s'
)
    comment '视频文件信息';

create table video_info_post
(
    video_id         varchar(10) default '0' not null comment '视频id'
        primary key,
    video_cover      varchar(50)             not null comment '视频封面',
    video_name       varchar(100)            not null comment '视频名称',
    user_id          varchar(10)             not null comment '用户id',
    create_time      datetime                not null comment '创建时间',
    last_update_time datetime                not null comment '最后更新时间',
    p_category_id    int                     not null comment '父级分类id',
    category_id      int                     null comment '分类id',
    status           tinyint(1)              not null comment '0转码中1转码失败2待审核3审核成功4审核失败',
    post_type        tinyint                 not null comment '0自制作1转载',
    origin_info      varchar(200)            null comment '原资源说明',
    tags             varchar(300)            null comment '标签',
    introduction     varchar(200)            null comment '简介',
    interaction      varchar(5)              null comment '互动设置',
    duration         int         default 0   null comment '持续时间'
)
    comment '审核中的信息表';

create index video_info_post_category_id_index
    on video_info_post (category_id);

create index video_info_post_create_time_index
    on video_info_post (create_time);

create index video_info_post_p_category_id_index
    on video_info_post (p_category_id);

create index video_info_post_user_id_index
    on video_info_post (user_id);

create index video_info_post_video_id_index
    on video_info_post (video_id);

create table video_play_history
(
    user_id          varchar(10) not null,
    video_id         varchar(10) not null comment '视频id',
    file_index       int         null comment '文件索引',
    last_update_time datetime    null comment '最后更新时间',
    primary key (user_id, video_id)
);

create index video_play_history_user_id_index
    on video_play_history (user_id);

create index video_play_history_video_id_index
    on video_play_history (video_id);

