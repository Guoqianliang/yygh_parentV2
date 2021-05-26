package com.gql.yygh.user.api;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.common.utils.AuthContextHolder;
import com.gql.yygh.model.user.Patient;
import com.gql.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 就诊人管理
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

    // 根据id获取就诊人信息

    // 修改就诊人信息

    // 删除就诊人信息
}
