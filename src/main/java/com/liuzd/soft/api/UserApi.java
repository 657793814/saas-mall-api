package com.liuzd.soft.api;

import com.liuzd.soft.annotation.NoLogin;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.service.BuyerService;
import com.liuzd.soft.service.CaptchaResult;
import com.liuzd.soft.service.CaptchaService;
import com.liuzd.soft.vo.ResultMessage;
import com.liuzd.soft.vo.user.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserApi {

    private final CaptchaService captchaService;
    private final BuyerService buyerService;

    @PostMapping("/register")
    @NoLogin
    public ResultMessage<RegisterResp> register(@RequestBody RegisterReq registerReq, HttpSession session) {
        String captcha = registerReq.getCaptcha();
        if (!validCaptcha(session, captcha)) {
            return ResultMessage.fail(RetEnums.FAIL.getCode(), "验证码错误");
        }
        return ResultMessage.success(buyerService.register(registerReq));
    }

    @Nullable
    private boolean validCaptcha(HttpSession session, String captcha) {
        String captchaCode = (String) session.getAttribute(GlobalConstant.CAPTCHA_CODE);
        boolean isValid = captchaService.validateCaptcha(captcha, captchaCode);
        if (isValid) {
            // 验证成功后清除验证码
            session.removeAttribute("CAPTCHA_CODE");
            return true;
        } else {
            return false;
        }

    }

    @PostMapping("/login")
    @NoLogin
    public ResultMessage<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
        return ResultMessage.success(buyerService.login(loginReq));
    }

    @PostMapping("/un_login")
    public String unLogin() {
        return "success";
    }

    /**
     * 获取验证码图片
     *
     * @param session HTTP会话
     * @return 验证码图片和相关信息
     */
    @GetMapping("/captcha")
    @NoLogin
    public ResultMessage<CaptchaResult> getCaptcha(HttpSession session) {
        CaptchaResult captchaResult = captchaService.generateCaptcha();

        // 将验证码存储在会话中，用于后续验证
        session.setAttribute(GlobalConstant.CAPTCHA_CODE, captchaResult.getCaptchaCode());

        return ResultMessage.success(captchaResult);
    }

    /**
     * 验证验证码
     *
     * @param captcha 用户输入的验证码
     * @param session HTTP会话
     * @return 验证结果
     */
    @PostMapping("/verify-captcha")
    @NoLogin
    public ResultMessage<Object> verifyCaptcha(@RequestParam("captcha") String captcha, HttpSession session) {
        if (!validCaptcha(session, captcha)) {
            return ResultMessage.fail(RetEnums.FAIL.getCode(), "验证码错误");
        }
        return ResultMessage.success("success");
    }

    @RequestMapping("/addresses")
    public ResultMessage<List<AddressResp>> addresses() {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);
        return ResultMessage.success(buyerService.addresses(tokenInfo.getUid()));
    }

    @RequestMapping("/add_address")
    public ResultMessage<Object> addAddress(@Valid @RequestBody EditAddressReq req) {
        buyerService.addAddress(req);
        return ResultMessage.success("success");
    }

    @RequestMapping("/edit_address")
    public ResultMessage<Object> editAddress(@Valid @RequestBody EditAddressReq req) {
        buyerService.editAddress(req);
        return ResultMessage.success("success");
    }


}
