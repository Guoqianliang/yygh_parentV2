package com.gql.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.gql.yygh.cmn.client.DictFeignClient;
import com.gql.yygh.hosp.repository.HospitalRepository;
import com.gql.yygh.hosp.service.HospitalService;
import com.gql.yygh.model.hosp.Hospital;
import com.gql.yygh.vo.hosp.HospitalQueryVo;
import com.gql.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 医院管理Service实现类
 * @author Guoqianliang
 * @date 17:06 - 2021/4/18
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    /**
     * 注入hospitalRepository
     */
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 上传医院
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        // 1.把参数map集合转换为Hospital对象(借助JSONObject工具)
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 2.判断MongoDB中是否已有这条记录
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        // 3.如果有就执行更新,没有就执行保存
        if (null != hospitalExist) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    /**
     * 查询医院
     * @param hoscode
     * @return
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    // 医院列表(条件查询带分页)
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 1.创建pageable对象
        Pageable pageable = PageRequest.of(page - 1, limit);
        // 2.创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        // 3.hospitalQueryVo转换为Hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        // 4.创建对象
        Example<Hospital> example = Example.of(hospital, matcher);
        // 5.调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        // 6.得到所有医院信息的集合
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });
        return pages;
    }

    // 更新医院上线状态
    @Override
    public void updateStatus(String id, Integer status) {
        // 根据id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
        // 设置修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    // 医院详情信息
    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        // 医院基本信息(包含医院等级)
        result.put("hospital", hospital);
        // 预约信息
        result.put("bookingRule", hospital.getBookingRule());
        // 不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    // 根据编号获取医院名
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }


    // 获取查询list集合,遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        // 封装医院等级
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        hospital.getParam().put("hostypeString", hostypeString);
        // 封装医院省市区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString);
        return hospital;
    }
}
