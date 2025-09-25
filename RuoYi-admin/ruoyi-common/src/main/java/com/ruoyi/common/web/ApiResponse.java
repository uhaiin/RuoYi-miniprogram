package com.ruoyi.common.web;

import com.ruoyi.common.enums.ApiResponseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 请求结果封装
 *
 * @param <T> 泛型
 * @author jiancai.zhong
 */
@Getter
@Setter
@Schema(description = "微信请求结果封装")
public class ApiResponse<T> implements Serializable {

    /**
     * 默认构造器，创建一个 ExampleClass 的实例。
     */
    public ApiResponse() {
        // 构造器的具体实现
    }

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    @Schema(title = "是否请求成功")
    protected boolean success = true;

    /**
     * 请求结果代码
     */
    @Schema(title = "请求结果代码")
    private Integer code = ApiResponseEnum.SUCCESS.code;

    /**
     * 请求结果消息
     */
    @Schema(title = "请求结果消息")
    private String message = ApiResponseEnum.SUCCESS.message;

    /**
     * 请求结果数据
     */
    @Schema(title = "请求结果数据")
    private T data;

    /**
     * 请求时候的时间戳
     */
    @Schema(title = "请求时间戳")
    private Long timestamp = System.currentTimeMillis();

    /**
     * 构建一个表示成功响应的ApiResponse对象 该方法用于在API调用成功时，创建并返回一个包含成功标志和数据的响应对象
     *
     * @param data 成功返回的数据，可以是任何类型
     * @param <T>  泛型参数，表示返回数据的类型
     * @return 返回一个ApiResponse对象，其中包含成功标志和返回数据
     */
    public static <T> ApiResponse<T> success(T data) {
        // 创建一个新的ApiResponse对象
        ApiResponse<T> apiResponse = new ApiResponse<>();
        // 设置ApiResponse对象的数据
        apiResponse.setData(data);
        // 设置ApiResponse对象的成功标志为true
        apiResponse.setSuccess(true);
        // 返回填充了数据和成功标志的ApiResponse对象
        return apiResponse;
    }

    /**
     * 创建一个表示失败的API响应对象 此方法用于生成一个失败状态的API响应，可以包含任意类型的响应数据
     *
     * @param data 响应的数据，可以是任意类型
     * @param <T>  泛型参数，表示响应数据的类型
     * @return 返回一个表示失败状态的API响应对象
     */
    public static <T> ApiResponse<T> failure(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setData(data);
        apiResponse.setCode(ApiResponseEnum.FAILURE.code);
        apiResponse.setMessage(ApiResponseEnum.FAILURE.message);
        apiResponse.setSuccess(false);
        return apiResponse;
    }

    /**
     * 创建一个表示成功的API响应对象
     *
     * @param message 成功消息，用于描述操作成功的原因
     * @param <T>     泛型参数，表示API响应中可能包含的数据类型
     * @return 返回一个ApiResponse对象，表示操作成功
     * <p>
     * 此方法用于生成一个表示成功操作的API响应实例它初始化一个ApiResponse对象， 设置成功消息，并将成功标志设置为true，然后返回这个ApiResponse对象
     */
    public static <T> ApiResponse<T> success(String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(message);
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    /**
     * 创建一个表示成功的API响应对象
     *
     * @param <T> 泛型参数，表示API响应中可能包含的数据类型
     * @return ApiResponse<T> 返回一个ApiResponse对象，表示操作成功
     * <p>
     * 此方法用于生成一个表示成功操作的API响应实例它初始化一个ApiResponse对象， 设置成功消息，并将成功标志设置为true，然后返回这个ApiResponse对象
     * @author Jiancai.zhong
     * @date 2025-05-08 02:03:51
     */
    public static <T> ApiResponse<T> ok() {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("ok");
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    /**
     * 创建一个表示失败的API响应对象
     *
     * @param message 错误消息，用于描述失败的原因
     * @param <T>     泛型标记，表示响应数据的类型
     * @return 返回一个表示失败的ApiResponse对象，包含相应的错误消息和状态码
     */
    public static <T> ApiResponse<T> failure(String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(message);
        apiResponse.setCode(ApiResponseEnum.FAILURE.code);
        apiResponse.setSuccess(false);
        return apiResponse;
    }

    @SuppressWarnings("unused")
    private void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}