package com.ruoyi.common.config.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 *
 * @author ruoyi
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {
    /**
     * 作者
     */
    public String appId;

    /**
     * 生成包路径
     */
    public String secret;
}
