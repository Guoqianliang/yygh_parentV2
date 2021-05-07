package com.gql.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.hosp.HospitalSet;

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
}
