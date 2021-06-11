package com.gql.yygh.msm.service;

import com.gql.yygh.vo.msm.MsmVo;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface MsmService {
    // 发送手机验证码
    boolean send(String phone, String code);

    // mq使用的发送短信
    boolean send(MsmVo msmVo);
}
