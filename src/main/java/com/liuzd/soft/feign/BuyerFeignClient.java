package com.liuzd.soft.feign;

import com.liuzd.soft.feign.fallback.BuyerFeignClientFallback;
import com.liuzd.soft.interceptor.FeignClientInterceptor;
import com.liuzd.soft.vo.user.LoginReq;
import com.liuzd.soft.vo.user.LoginResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: liuzd
 * @date: 2025/9/17
 * @email: liuzd2025@qq.com
 * @desc FeignClient 配置
 * name: consul注册的微服务名（基于consul的服务发现机制实现的负载均衡）
 */
@FeignClient(name = "${feign.server-name.saas-buyer-api}"
        , configuration = FeignClientInterceptor.class
        , fallback = BuyerFeignClientFallback.class)
@Component
public interface BuyerFeignClient {
    //todo 定义user微服务接口函数
    @RequestMapping("/test/hello")
    String helloWorld();

    @PostMapping("/login")
    @ResponseBody
    LoginResp login(@RequestBody LoginReq loginReq);
}
