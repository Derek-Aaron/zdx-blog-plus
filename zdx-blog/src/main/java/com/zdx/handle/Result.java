package com.zdx.handle;

import com.zdx.utils.MessageUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaodengxuan
 */
@Data
@Schema(description = "相应实体")
public class Result<T> implements Serializable {

	@Schema(description = "相应码")
	private int code;

	@Schema(description = "相应消息")
	private String message;

	@Schema(description = "相应数据")
	private T data;

	public Result(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Result() {
	}

	public static <T> Result<T> success(int code, String message, T data) {
		return new Result<>(code, message, data);
	}

	public static <T> Result<T> success(int code, String message) {
		return success(code, message, null);
	}

	public static <T> Result<T> success(String message) {
		return success(200, message);
	}
	public static <T> Result<T> success(String message, T data) {
		return success(200, message, data);
	}
	public static <T> Result<T> success(T data) {
		return success(200, MessageUtil.message("zdx.operate.success"), data);
	}

	public static <T> Result<T> success() {
		return success(200, MessageUtil.message("zdx.operate.success"));
	}


	public static <T> Result<T> error(int code, String message, T data) {
		return new Result<>(code, message, data);
	}

	public static <T> Result<T> error(int code, String message) {
		return error(code, message, null);
	}

	public static <T> Result<T> error(String message) {
		return error(500, message);
	}
	public static <T> Result<T> error() {
		return error(500, MessageUtil.message("zdx.operate.error"));
	}
}
