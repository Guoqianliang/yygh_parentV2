package com.gql.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.hosp.mapper.HospitalSetMapper;
import com.gql.yygh.hosp.service.HospitalSetService;
import com.gql.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

/**
 * @Description: 医院设置Service实现类
 * @author Guoqianliang
 * @date 0:12 - 2021/4/4
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
        implements HospitalSetService {
    /**
     * 根据医院编码获取SignKey
     * @param hoscode
     * @return
     */
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(queryWrapper);
        return hospitalSet.getSignKey();
    }
}
