package com.ruoyi.wechat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.config.wechat.WeChatConfig;
import com.ruoyi.wechat.domain.WeChatSessionResponse;
import com.ruoyi.wechat.service.WeChatAuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeChatAuthServiceImpl implements WeChatAuthService {

    @Resource
    private WeChatConfig weChatConfig;

    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public WeChatSessionResponse getSessionInfo(String jsCode) {
        String appId = weChatConfig.getAppId();
        String secret = weChatConfig.getSecret();
        String response = RestClient.create()
                .get()
                .uri(WECHAT_API_URL + "?appid={appid}&secret={secret}&js_code={jscode}&grant_type=authorization_code",
                        appId, secret, jsCode)
                .retrieve()
                .body(String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, WeChatSessionResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("解析微信API响应失败", e);
        }
    }
}