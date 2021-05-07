package com.gql.yygh.hosp.service;

import com.gql.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;
import com.gql.yygh.model.hosp.Department;
import com.gql.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:医院科室-Service接口
 * @author Guoqianliang
 * @date 8:55 - 2021/4/20
 */
public interface DepartmentService {
    /**
     * 上传科室
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询科室
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室接口
     * @param hoscode
     * @param depcode
     */
    void remove(String hoscode, String depcode);

    // 查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    /**
     * 根据医院编号、科室编号 获取科室名称
     * @param hoscode
     * @param depcode
     * @return
     */
    String getDepName(String hoscode, String depcode);
}
