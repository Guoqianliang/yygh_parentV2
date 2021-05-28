package com.gql.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.vo.user.LoginVo;
import com.gql.yygh.vo.user.UserAuthVo;
import com.gql.yygh.vo.user.UserInfoQueryVo;

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
    // 传递用户id、认证数据vo对象
    void userAuth(Long userId, UserAuthVo userAuthVo);
    // 用户列表接口(条件分页带查询)
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);
    // 用户锁定
    void lock(Long userId, Integer status);
    // 用户详情
    Map<String, Object> show(Long userId);
}
