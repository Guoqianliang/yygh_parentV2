package com.gql.yygh.common.utils;

import com.gql.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:获取当前用户信息的工具类
 *      可以获取用户id、获取用户名称
 * @author Guoqianliang
 */
public class AuthContextHolder {
    // 获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("token");
        // 使用jwt从token中获取userid
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    // 获取用户名称
    public static String getUserName(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("token");
        // 使用jwt从token中获取userName
        String userName = JwtHelper.getUserName(token);
        return userName;
    }

}
