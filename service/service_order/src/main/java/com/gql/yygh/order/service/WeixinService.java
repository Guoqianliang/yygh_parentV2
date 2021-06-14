package com.gql.yygh.order.service;

import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface WeixinService {
    /**
     * 下单 生成二维码
     */
    Map createNative(Long orderId);
}
