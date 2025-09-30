package com.miniBili.entity.constants;

public class Constants {
    public static final String REDIS_KEY_PREFIX = "miniBili:";
    public static final String FILE_FOLDER = "file/";
    public static final String FILE_COVER = "cover/";
    public static final String FILE_VIDEO = "video/";
    public static final String FILE_TEMP = "temp/";
    public static final String FILE_MP4 = "/temp.mp4";
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkCode";
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web";
    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PREFIX + "token:admin";
    public static final String TOKEN_WEB = "token";
    public static final String TOKEN_ADMIN = "adminToken";
    public static final Integer REDIS_KEY_EXPIRE_ONE_MIN = 1000 * 60;
    public static final Integer REDIS_KEY_EXPIRE_ONE_DAY = 1000 * 60 * 60 *24;//毫秒
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_2 = 2;
    public static final Integer ONE = 1;
    public static final Integer ZERO = 0;
    public static final Long MB = 1024*1024L;
    public static final String REDIS_KEY_CATEGORY_LIST = REDIS_KEY_PREFIX + "category:list:";
    public static final String IMAGE_THUM_SUFFIX = "_thumbnail.jpg";
    public static final String REDIS_KEY_UPLOADING_FILE = REDIS_KEY_PREFIX + "uploading:";
    public static final String REDIS_KEY_SYS_SETTING = REDIS_KEY_PREFIX + "sysSetting:";
    public static final String REDIS_KEY_FILE_DEL = REDIS_KEY_PREFIX + "file:list:del:";
    public static final String REDIS_KEY_QUEUE_TRANSFER = REDIS_KEY_PREFIX + "queue:transfer:";
    public static final String VIDEOS_CODE_HEVC = "hevc";
    public static final String TS_NAME = "index.ts";
    public static final String M3U8_NAME = "index.m3u8";
    public static final String VIDEO_CODE_TEMP_SUFFIX = "_temp.mp4";
}
