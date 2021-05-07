package com.gql.yygh.hosp.repository;

import com.gql.yygh.model.hosp.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description: 科室-数据访问层
 * @author Guoqianliang
 * @date 8:53 - 2021/4/20
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    /**
     * 根据医院编号和科室编号查询科室信息
     * @param hoscode
     * @param depcode
     * @return
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
