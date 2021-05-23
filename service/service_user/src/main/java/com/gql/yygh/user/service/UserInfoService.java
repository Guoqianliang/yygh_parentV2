package com.gql.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface UserInfoService extends IService<UserInfo> {
    // 用户手机号登录接口
    Map<String, Object> loginUser(LoginVo loginVo);

    // 判断数据库是否存在微信的扫描人信息
    UserInfo selectWxInfoOpenId(String openid);
}
