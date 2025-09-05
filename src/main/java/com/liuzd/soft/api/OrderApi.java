package com.liuzd.soft.api;

import com.liuzd.soft.vo.ResultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单相关api
 *
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderApi {

    /**
     * 计算价格
     *
     * @return
     */
    @PostMapping(path = "/computer")
    public ResultMessage<Object> computer() {
        return ResultMessage.success("success");
    }

    /**
     * 确认订单
     * 创建订单信息
     * 返回订单数+支付方式数据
     *
     * @return
     */
    @PostMapping(path = "/confirm")
    public ResultMessage<Object> confirm() {
        return ResultMessage.success("success");
    }

    /**
     * 支付订单
     * 根据支付方式创建支付单
     * 返回支付信息给前端，例如 微信支付jsapi参数等，本地钱包支付唤起支付密码输入等
     *
     * @return
     */
    @PostMapping(path = "/create_pay")
    public ResultMessage<Object> createPay() {
        return ResultMessage.success("success");
    }

    /**
     * 确认支付
     * 1. 本地账户支付，服务端验证支付密码处理支付确认
     * 2. 微信唤起支付 前端确认支付后调用，服务端查询微信api处理支付确认
     * 3. 支付宝唤起支付 前端确认支付后调用，服务端查询支付宝api处理支付确认
     * 等等
     *
     * @return
     */
    @PostMapping(path = "/do_pay")
    public ResultMessage<Object> doPay() {
        return ResultMessage.success("success");
    }


}
