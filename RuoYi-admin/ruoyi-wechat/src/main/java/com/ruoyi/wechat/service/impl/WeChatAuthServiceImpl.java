package com.ruoyi.wechat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.config.wechat.WeChatConfig;
import com.ruoyi.wechat.domain.WeChatSessionResponse;
import com.ruoyi.wechat.service.WeChatAuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeChatAuthServiceImpl implements WeChatAuthService {

    @Resource
    private WeChatConfig weChatConfig;

    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获取微信会话信息
     * 通过微信提供的js_code向微信服务器请求获取会话信息，包括openid和session_key等
     *
     * @param jsCode 微信登录凭证code，通过wx.login获取
     * @return WeChatSessionResponse 微信会话响应对象，包含openid、session_key等信息
     */
    @Override
    public WeChatSessionResponse getSessionInfo(String jsCode) {
        // 调用微信API获取会话信息
        String response = RestClient.create()
                .get()
                .uri(WECHAT_API_URL + "?appid={appid}&secret={secret}&js_code={jscode}&grant_type=authorization_code",
                        weChatConfig.getAppId(), weChatConfig.getSecret(), jsCode)
                .retrieve()
                .body(String.class);

        // 解析微信API响应结果
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, WeChatSessionResponse.class);
        } catch (Exception e) {
            log.error("解析微信API响应失败 ==> {}", e.getMessage(), e);
            throw new RuntimeException("解析微信API响应失败", e);
        }
    }

}