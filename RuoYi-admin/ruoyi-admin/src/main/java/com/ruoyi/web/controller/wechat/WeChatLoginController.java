package com.ruoyi.web.controller.wechat;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.web.ApiResponse;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.wechat.domain.WeChatSessionResponse;
import com.ruoyi.wechat.domain.WeChatUser;
import com.ruoyi.wechat.service.WeChatAuthService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeChatLoginController {


    @Resource
    private WeChatAuthService weChatAuthService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ISysUserService sysUserService;


    @GetMapping("/wechat/login/{code}")
    public ApiResponse<WeChatUser> login(@PathVariable String code) {
        WeChatUser wechatUser = new WeChatUser();
        WeChatSessionResponse weChatSession = weChatAuthService.getSessionInfo(code);
        String openid = weChatSession.getOpenid();
        if (openid != null) {

            SysUser sysUser = sysUserService.selectUserByOpenId(openid);
            if (sysUser == null) {
                sysUser = sysUserService.registerWeChatUser(openid);
            }
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(sysUser);
            String token = tokenService.createToken(loginUser);
            wechatUser.setToken(token);
            BeanUtils.copyProperties(loginUser.getUser(), wechatUser);
           return ApiResponse.success(wechatUser);
        } else {
            return ApiResponse.failure("登录失败");
        }
    }
}
