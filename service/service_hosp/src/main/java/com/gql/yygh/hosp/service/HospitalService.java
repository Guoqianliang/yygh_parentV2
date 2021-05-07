package com.gql.yygh.hosp.service;

import com.gql.yygh.model.hosp.Hospital;
import com.gql.yygh.vo.hosp.HospitalQueryVo;
import com.gql.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Description: 医院g管理-Service接口
 * @author Guoqianliang
 * @date 17:05 - 2021/4/18
 */
public interface HospitalService {
    /**
     * 上传医院接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询医院接口
     * @param hoscode
     * @return
     */
    Hospital getByHoscode(String hoscode);
    // 医院列表(条件查询带分页)
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
    // 更新医院上线状态
    void updateStatus(String id, Integer status);

    // 医院详情信息
    Map<String, Object> getHospById(String id);
    // 根据编号获取医院名
    String getHospName(String hoscode);
}

