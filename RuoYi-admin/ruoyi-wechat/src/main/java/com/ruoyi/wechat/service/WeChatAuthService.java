package com.ruoyi.wechat.service;


import com.ruoyi.wechat.domain.WeChatSessionResponse;

public interface WeChatAuthService {

    WeChatSessionResponse getSessionInfo(String jsCode);
}