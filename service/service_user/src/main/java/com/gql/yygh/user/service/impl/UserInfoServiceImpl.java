package com.gql.yygh.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.common.exception.YyghException;
import com.gql.yygh.common.helper.JwtHelper;
import com.gql.yygh.common.result.ResultCodeEnum;
import com.gql.yygh.enums.AuthStatusEnum;
import com.gql.yygh.model.acl.User;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.user.mapper.UserInfoMapper;
import com.gql.yygh.user.service.UserInfoService;
import com.gql.yygh.vo.user.LoginVo;
import com.gql.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 用户手机号登录接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        // 从loginVo获取输入的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        // 校验验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if (null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

        // 如果userInfo为空,进行正常的手机登录
        if (null == userInfo) {
            // 判断是否是第一次登录:根据手机号查询数据库
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);

            // 如果是第一次使用手机登录
            if (userInfo == null) {
                // 添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }


        // 校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 不是第一次,就直接登录
        // 返回登录信息
        // 返回登录用户名
        // 返回tocken信息
        HashMap<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        // 如果用户名称为空，就去得到昵称
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        // 如果昵称还为空，就去得到手机号
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        // 使用JWT生成tocken字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("tocken", token);
        return map;
    }

    // 判断数据库是否存在微信的扫描人信息
    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        return userInfo;
    }

    // 传递用户id、认证数据vo对象
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        // 根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        // 设置认证信息
        // 认证人姓名
        userInfo.setName(userAuthVo.getName());
        // 其他认证信息：类型、编号、证书地址、认证状态
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());

        // 进行信息更新
        baseMapper.updateById(userInfo);
    }
}















