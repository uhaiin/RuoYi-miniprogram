package com.ruoyi.wechat.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "微信用户实体")
public class WeChatUser {
    private Long userId;
    private String nickName;
    private String userName;
    private int sex;
    private String token;
    private boolean admin;
    private String email;
}
