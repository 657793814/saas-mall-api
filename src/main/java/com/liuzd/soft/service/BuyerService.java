package com.liuzd.soft.service;

import com.liuzd.soft.vo.user.*;

import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
public interface BuyerService {

    RegisterResp register(RegisterReq req);

    LoginResp login(LoginReq loginReq);

    List<AddressResp> addresses(Integer uid);

    void addAddress(EditAddressReq req);

    void editAddress(EditAddressReq req);
}
