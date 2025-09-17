package com.ruoyi.web.controller.wechat;

import com.ruoyi.common.web.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JasyptController {

    @GetMapping("/wechat/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("hello world");
    }
}
