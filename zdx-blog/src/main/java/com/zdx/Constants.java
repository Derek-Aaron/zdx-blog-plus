package com.zdx;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public final static Integer REDIS_TIME = 5;

    public final static String ADMIN_AUTH = "*::||::*";

    public static final Integer DEFAULT_EXPIRE_DAY = 10;

    public static final String JWT_USER_INFO_KEY = "zdx-user";

    public static final String ACL_ROLE_PREFIX = "us:role:";

    public static final String ACL_USER_PREFIX = "us:user:";
    public static final String ACL_MENU_PREFIX = "tk:menu:";

    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    public static final String ROUTER_KEY = "zdx:router:";
    public static final String DICT_KEY = "zdx:dict:";
    public static final String AUTHORIZATION = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final Long EXPIRETIME = 15L;

    public static final String FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";
    public static final String MUSIC_CACHE = "music_cache";
    public static final String ZDX_KEY_GENERATOR = "zdx-key-generator";

    public static final List<String> IMAGE_TYPE = Arrays.asList(".jpg",".png",".gif",".bmp",".eps",".jpeg");
    public static final List<String> DOCUMENT_TYPE = Arrays.asList(".xls",".xlsx",".doc",".md",".docx",".pdf");
    public static final List<String> AUDIO_TAPE = Arrays.asList(".mp3",".m4a",".cda",".wav",".aif",".aiff",".wma",".ape",".oggl");
    public static final List<String> VIDEO_TAPE = Arrays.asList(".mp4",".avi",".wmv",".mpg",".mpeg",".mov", ".ram", ".rm");

    public static final Long DOWNSIZE = 1024 * 1024 * 30L;


    public static final String BLOG_SITE_CONFIG = "blog_site_config";
    public static final String UNIQUE_VISITOR = "unique_visitor";

    public static final String BLOG_VIEW_COUNT = "blog_view_count";

    public static final String CODE_KEY = "code:";
    public static final Integer CODE_EXPIRE_TIME = 2;
    public static final Long BLOGGER_ID = 1676767825482121217L;

    /**
     * 评论提醒
     */
    public static final String COMMENT_REMIND = "评论提醒";

    /**
     * 作者邮件HTML模板
     */
    public static final String AUTHOR_TEMPLATE = "author.html";

    /**
     * 用户邮件HTMl模板
     */
    public static final String USER_TEMPLATE = "user.html";



}
