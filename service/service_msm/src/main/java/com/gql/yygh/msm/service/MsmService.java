package com.gql.yygh.msm.service;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface MsmService {
    // 发送手机验证码
    boolean send(String phone, String code);
}
