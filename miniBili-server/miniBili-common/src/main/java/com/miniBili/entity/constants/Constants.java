package com.miniBili.entity.constants;

public class Constants {
    public static final String REDIS_KEY_PREFIX = "miniBili:";
    public static final String FILE_FOLDER = "file/";
    public static final String FILE_COVER = "cover/";
    public static final String FILE_VIDEO = "video/";
    public static final String FILE_TEMP = "temp/";
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkCode";
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web";
    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PREFIX + "token:admin";
    public static final String TOKEN_WEB = "token";
    public static final String TOKEN_ADMIN = "adminToken";
    public static final Integer REDIS_KEY_EXPIRE_ONE_MIN = 1000 * 60;
    public static final Integer REDIS_KEY_EXPIRE_ONE_DAY = 1000 * 60 * 60 *24;//毫秒
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_30 = 30;
    public static final Integer ONE = 1;
    public static final Integer ZERO = 0;
    public static final String REDIS_KEY_CATEGORY_LIST = REDIS_KEY_PREFIX + "category:list:";
    public static final String IMAGE_THUM_SUFFIX = "_thumbnail.jpg";

}
