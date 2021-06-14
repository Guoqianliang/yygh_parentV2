package com.gql.yygh.user.api;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.common.utils.AuthContextHolder;
import com.gql.yygh.model.user.Patient;
import com.gql.yygh.user.service.PatientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 就诊人管理接口
 * @author Guoqianliang
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    // 获取就诊人列表接口
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        // 获取当前id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    // 添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        // 获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    // 根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    // 修改就诊人信息
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    // 删除就诊人信息
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据就诊人id获取就诊人信息")
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(
            @PathVariable("id") Long id) {
        Patient patient = patientService.getPatientId(id);
        return patient;
    }

}
