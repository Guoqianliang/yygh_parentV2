package com.gql.yygh.user.controller;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.common.utils.AuthContextHolder;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.user.service.UserInfoService;
import com.gql.yygh.vo.user.LoginVo;
import com.gql.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    // 用户手机号登录接口
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    // 用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        // 传递用户id、认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return Result.ok();
    }

    // 获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
