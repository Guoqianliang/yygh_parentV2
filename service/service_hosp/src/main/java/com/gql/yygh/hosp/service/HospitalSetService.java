package com.gql.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.hosp.HospitalSet;
import com.gql.yygh.vo.order.SignInfoVo;

/**
 * @Description: 医院设置-Service接口
 * @author Guoqianliang
 * @date 0:10 - 2021/4/4
 */
public interface HospitalSetService extends IService<HospitalSet> {

    /**
     * 根据医院编码获取SignKey
     * @param hoscode
     * @return
     */
    String getSignKey(String hoscode);
    // 获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
