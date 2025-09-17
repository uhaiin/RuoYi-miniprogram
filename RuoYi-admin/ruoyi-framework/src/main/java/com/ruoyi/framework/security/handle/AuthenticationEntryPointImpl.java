package com.ruoyi.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    @Serial
    private static final long serialVersionUID = -8970718410437077606L;

        /**
     * 当用户尝试访问受保护的资源但未通过身份验证时，此方法会被调用
     * 用于处理认证入口点，返回未授权的错误响应
     *
     * @param request  HTTP请求对象，包含客户端的请求信息
     * @param response HTTP响应对象，用于向客户端返回响应数据
     * @param e        认证异常对象，包含认证失败的具体信息
     * @throws IOException 当IO操作出现异常时抛出
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        // 设置HTTP状态码为401（未授权）
        int code = HttpStatus.UNAUTHORIZED;
        // 构造认证失败的错误消息，包含请求的URI
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        // 将错误信息以JSON格式返回给客户端
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }

}
