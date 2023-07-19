package com.zdx.event;

import cn.hutool.core.util.ObjUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaodengxuan
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class EventObject extends ApplicationEvent {
	private  String name;

	private Map<String, Object> attributes;

	public EventObject(Object source) {
		super(source);
		this.name = null;
		this.attributes = new HashMap<>();
	}

	public EventObject(Object source, String name) {
		super(source);
		this.name = name;
		this.attributes = new HashMap<>();
	}

	public EventObject(Object source, Object... args) {
		super(source);
		this.name = null;
		this.attributes = new HashMap<>();
		this.attributes.put(Attribute.ARGUMENTS, Arrays.asList(args));
	}

	public EventObject(Object source, String name, Object... args) {
		super(source);
		this.name = name;
		this.attributes = new HashMap<>();
		this.attributes.put(Attribute.ARGUMENTS, Arrays.asList(args));
	}

	public <T> T getSource(Class<T> clz) {
		return clz.cast(this.source);
	}

	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	public <T> T getAttribute(String name, Class<T> clz) {
		Object obj = this.attributes.get(name);
		return ObjUtil.isNotNull(obj) ? clz.cast(obj) : null;
	}

	public EventObject setAttribute(String name, Object value) {
		this.attributes.put(name, value);
		return this;
	}

	public static final class Attribute {
		//方法参数
		public static final String ARGUMENTS = "arguments";
		//登录后事件
		public static final String LOGINLOG = "loginlog";
		public static final String REFRESH_LOGIN_TOKEN_CACHE = "refresh_login_token_cache";
		public static final String LOG_SAVE = "log_save";
		public static final String USER_SESSION = "user_session";
		public static final String REQUEST = "request";
		public static final String THROWABLE = "throwable";
		public static final String STOP_WATCH = "stop_Watch";

		public static final String ARTICLE_ID = "article_id";

		public static final String INSERT_VIEW_COUNT = "insert_view_count";
		public static final String VIEW_COUNT = "view_count";

		public static final String LOGOUT = "logout";
		public static final String DELCONTENTCACHE = "delContent";
		public static final String SAVEORUPDATETAGS = "saveOrUpdateTags";
		public static final String CREATEDICTDATA = "createDictData";
		public static final String ES_CREATE_UPDATE = "esCreateUpdate";
		public static final String SENDMAIL = "sendMail";
		/**
		 * 方法参数快照
		 */
		public static final String ARGUMENTS_SNAPSHOTS = "arguments-snapshots";
		/**
		 * 方法返回值
		 */
		public static final String RESULT = "result";
	}
}
