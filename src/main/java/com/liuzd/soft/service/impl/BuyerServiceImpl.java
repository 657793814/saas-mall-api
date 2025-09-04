package com.liuzd.soft.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dao.PBuyerAddressDao;
import com.liuzd.soft.dao.PBuyerDao;
import com.liuzd.soft.dto.PBuyerDto;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.entity.PBuyerAddressEntity;
import com.liuzd.soft.entity.PBuyerEntity;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.BuyerService;
import com.liuzd.soft.utils.DateUtils;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.utils.SecureMd5Utils;
import com.liuzd.soft.utils.TokenUtils;
import com.liuzd.soft.vo.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    final PBuyerDao pBuyerDao;
    final PBuyerAddressDao pBuyerAddressDao;
    final RedisTemplate redisTemplate;

    @Override
    public RegisterResp register(RegisterReq req) {
        QueryWrapper<PBuyerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", req.getMobile());
        PBuyerEntity buyerEntity = pBuyerDao.selectOne(queryWrapper);
        Assert.isNull(buyerEntity, () -> MyException.exception(RetEnums.USER_EXIST, "手机号已存在"));

        PBuyerEntity pBuyerEntity = new PBuyerEntity();
        pBuyerEntity.setUuid(IdUtils.generateBuyerUUId());
        pBuyerEntity.setUname(req.getUsername());
        pBuyerEntity.setMobile(req.getMobile());
        String salt = SecureMd5Utils.generateSalt();
        String pwd = SecureMd5Utils.md5WithSalt(req.getPassword(), salt);
        pBuyerEntity.setSalt(salt);
        pBuyerEntity.setPassword(pwd);
        pBuyerEntity.setLevelValue(0);
        pBuyerEntity.setCreateTime(DateUtils.getCurrentDateTimeString());
        pBuyerEntity.setBirth("");
        pBuyerEntity.setSex(0);
        pBuyerDao.insert(pBuyerEntity);

        RegisterResp resp = new RegisterResp();
        resp.setUuid(pBuyerEntity.getUuid());
        resp.setUsername(pBuyerEntity.getUname());
        resp.setMobile(pBuyerEntity.getMobile());
        return resp;

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
        TokenInfo tokenInfo = new TokenInfo(buyerEntity.getId(), buyerEntity.getUuid(), buyerEntity.getUname(), randStr, timestamp);
        saveToken(token, tokenInfo);
        return loginResp;
    }

    public void saveToken(String token, TokenInfo tokenInfo) {
        redisTemplate.opsForValue().set(GlobalConstant.BUYER_TOKEN_CACHE_PREFIX + token, tokenInfo, 7, TimeUnit.DAYS);
    }

    public TokenInfo getTokenInfo(String token) {
        return (TokenInfo) redisTemplate.opsForValue().get(GlobalConstant.BUYER_TOKEN_CACHE_PREFIX + token);
    }

    public void delToken(String token) {
        redisTemplate.delete(GlobalConstant.BUYER_TOKEN_CACHE_PREFIX + token);
    }


    @Override
    public List<AddressResp> addresses(Integer uid) {

        QueryWrapper<PBuyerAddressEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<AddressResp> ret = new ArrayList<>();
        List<PBuyerAddressEntity> list = pBuyerAddressDao.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ret;
        }
        return list.stream().map(AddressResp::new).collect(Collectors.toList());

    }


    @Override
    public void addAddress(EditAddressReq req) {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);
        PBuyerAddressEntity entity = new PBuyerAddressEntity();
        fillAddressInfo(req, tokenInfo, entity, false);


    }

    @Override
    public void editAddress(EditAddressReq req) {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);

        //查询地址信息
        QueryWrapper<PBuyerAddressEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PBuyerAddressEntity::getId, req.getAddressId());
        PBuyerAddressEntity entity = pBuyerAddressDao.selectOne(queryWrapper);
        Assert.notNull(entity, () -> MyException.exception(RetEnums.ADDRESS_NOT_EXIST));
        if (entity.getUid() != tokenInfo.getUid()) {
            throw MyException.exception(RetEnums.ADDRESS_NOT_EXIST);
        }

        fillAddressInfo(req, tokenInfo, entity, true);
    }

    private void fillAddressInfo(EditAddressReq req, TokenInfo tokenInfo, PBuyerAddressEntity entity, boolean isEdit) {
        entity.setUid(tokenInfo.getUid());
        entity.setName(req.getReceiverName());
        entity.setMobile(req.getReceiverPhone());
        entity.setProvince(req.getProvinceId());
        entity.setCity(req.getCityId());
        entity.setDistrict(req.getDistrictId());
        entity.setDetail(req.getDetailAddress());
        entity.setIsDefault(req.getIsDefault() ? 1 : 0);
        if (isEdit) {
            pBuyerAddressDao.updateById(entity);
        } else {
            pBuyerAddressDao.insert(entity);
        }

    }
}
