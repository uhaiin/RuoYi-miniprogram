package com.ruoyi.wechat.domain;

import lombok.Data;

@Data
public class WeChatUser {
    private Long userId;
    private String nickName;
    private String userName;
    private int sex;
    private String token;
    private boolean admin;
    private String email;
}
