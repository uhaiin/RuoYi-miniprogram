package com.ruoyi.wechat.domain;

import lombok.Data;

@Data
public class WeChatSessionResponse {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;

}