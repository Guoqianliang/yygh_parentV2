package com.gql.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.common.exception.YyghException;
import com.gql.yygh.common.result.ResultCodeEnum;
import com.gql.yygh.hosp.mapper.HospitalSetMapper;
import com.gql.yygh.hosp.service.HospitalSetService;
import com.gql.yygh.model.hosp.HospitalSet;
import com.gql.yygh.vo.order.SignInfoVo;
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

    //获取医院签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);

        System.out.println("医院信息:" + hospitalSet);

        if (null == hospitalSet) {
            System.out.println("HospiatlSetServiceImpl类出现错误!");
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();

        // 打印测试
        System.out.println("医院API" + hospitalSet.getApiUrl());
        System.out.println("医院签名" + hospitalSet.getSignKey());

        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

}
