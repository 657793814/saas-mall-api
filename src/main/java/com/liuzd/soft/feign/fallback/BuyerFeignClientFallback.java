package com.liuzd.soft.feign.fallback;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuzd.soft.dao.PBuyerDao;
import com.liuzd.soft.dto.PBuyerDto;
import com.liuzd.soft.entity.PBuyerEntity;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.feign.BuyerFeignClient;
import com.liuzd.soft.utils.SecureMd5Utils;
import com.liuzd.soft.utils.TokenUtils;
import com.liuzd.soft.vo.user.LoginReq;
import com.liuzd.soft.vo.user.LoginResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: liuzd
 * @date: 2025/9/18
 * @email: liuzd2025@qq.com
 * @desc
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BuyerFeignClientFallback implements BuyerFeignClient {

    final PBuyerDao pBuyerDao;

    @Override
    public String helloWorld() {
        log.error("调用Buyer服务的helloWorld接口失败，触发降级逻辑");
        return "服务暂时不可用";
    }

    @Override
    public LoginResp login(LoginReq loginReq) {
        QueryWrapper<PBuyerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", loginReq.getUsername()).or().eq("mobile", loginReq.getUsername());
        PBuyerEntity buyerEntity = pBuyerDao.selectOne(queryWrapper);
        Assert.notNull(buyerEntity, () -> MyException.exception(RetEnums.USER_NOT_EXIST));

        if (!SecureMd5Utils.md5WithSalt(loginReq.getPassword(), buyerEntity.getSalt()).equals(buyerEntity.getPassword())) {
            throw MyException.exception(RetEnums.USERNAME_OR_PWD_ERROR);
        }

        LoginResp<PBuyerDto> loginResp = new LoginResp();
        PBuyerDto pBuyerDto = new PBuyerDto();
        pBuyerDto.setUuid(buyerEntity.getUuid());
        pBuyerDto.setUname(buyerEntity.getUname());
        pBuyerDto.setMobile(buyerEntity.getMobile());
        pBuyerDto.setSex(buyerEntity.getSex());
        pBuyerDto.setBirth(buyerEntity.getBirth());

        String token = TokenUtils.generateToken();
        String randStr = TokenUtils.generateRandStr();
        long timestamp = System.currentTimeMillis();

        loginResp.setToken(token);
        loginResp.setRandStr(randStr);
        loginResp.setTimestamp(timestamp);
        loginResp.setInfo(pBuyerDto);
        return loginResp;
    }
}
