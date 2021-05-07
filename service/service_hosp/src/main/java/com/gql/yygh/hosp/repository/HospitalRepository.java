package com.gql.yygh.hosp.repository;

import com.gql.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description: 医院管理-数据访问层
 * @author Guoqianliang
 * @date 17:03 - 2021/4/18
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    /**
     * 根据HosCode获得记录
     * @param hoscode
     * @return
     */
    Hospital getHospitalByHoscode(String hoscode);
}
