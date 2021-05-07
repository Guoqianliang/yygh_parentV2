package com.gql.yygh.hosp.controller.api;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.hosp.service.HospitalService;
import com.gql.yygh.model.hosp.Hospital;
import com.gql.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 前台医院API接口
 * @author Guoqianliang
 */
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo HospitalQueryVo) {

        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, HospitalQueryVo);
        List<Hospital> content = hospitals.getContent();
        int totalPages = hospitals.getTotalPages();
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname){
        List<Hospital> list = hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }

}
