package com.ruoyi.common.enums;

/**
 * ApiResponse code 枚举类
 *
 * @author Jiancai.zhong
 * @since 2024-10-21 10:03:29
 */
public enum ApiResponseEnum {

    /**
     * 成功
     */
    SUCCESS(0, "success"),

    /**
     * 失败
     */
    FAILURE(-1, "failure");

    /**
     * 请求结果代码，0 代表成功，-1 代表失败
     */
    public final Integer code;

    /**
     * 请求结果消息
     */
    public final String message;

    ApiResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}