package com.gql.yygh.hosp.controller.api;

import com.gql.yygh.hosp.service.ScheduleService;
import com.gql.yygh.model.hosp.Schedule;
import com.gql.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import com.gql.yygh.common.exception.YyghException;
import com.gql.yygh.common.helper.HttpRequestHelper;
import com.gql.yygh.common.result.Result;
import com.gql.yygh.common.result.ResultCodeEnum;
import com.gql.yygh.common.utils.MD5;
import com.gql.yygh.hosp.service.DepartmentService;
import com.gql.yygh.hosp.service.HospitalService;
import com.gql.yygh.hosp.service.HospitalSetService;
import com.gql.yygh.model.hosp.Department;
import com.gql.yygh.model.hosp.Hospital;
import com.gql.yygh.vo.hosp.DepartmentQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 医院管理Controller，此处接口需被外界调用
 * @author Guoqianliang
 * @date 17:14 - 2021/4/18
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 删除排班
     */
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取医院编号和排班编号
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");
        // 3.获取医院设置表中的密钥并进行MD5加密
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.removeSchedule(hoscode, hosScheduleId);
        return Result.ok();
    }

    /**
     * 查询排班
     */
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 3.获取医院编号,科室编号
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");
        // 当前页和每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setHoscode(depcode);
        // 执行查询科室操作
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    /**
     * 上传排班接口
     */
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取科室编号 和 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");
        // 3.获取医院设置表中的密钥并进行MD5加密
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        // 执行上传操作
        scheduleService.save(paramMap);
        return Result.ok();
    }


    /**
     * 删除科室接口
     * @param request
     * @return
     */
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        // 1.将传递过来的科室转换为Object类型
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 获取科室编号 和 医院编号
        String depcode = (String) paramMap.get("depcode");
        String hoscode = (String) paramMap.get("hoscode");
        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");
        // 3.获取医院设置表中的密钥并进行MD5加密
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    /**
     * 查询科室接口
     * @param request
     * @return
     */
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        // 1.将传递过来的科室转换为Object类型
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        // 3.获取医院编号
        String hoscode = (String) paramMap.get("hoscode");
        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");
        // 当前页和每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        // 执行查询科室操作
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "上传科室")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        // 1.将传递过来的数组类型转换为Object类型
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");

        // 3.获取医院设置表中的密钥并进行MD5加密
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 5.执行上传科室操作
        departmentService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "查询医院")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        // 1.将从医院管理表传递过来的医院信息转换为Object类型
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");

        // 3.获取医院设置表中的密钥并进行MD5加密
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 5.执行查询操作
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }


    @ApiOperation(value = "上传医院到数据库")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        // 1.将从医院管理表传递过来的医院信息转换为Object类型
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 2.获取医院管理表中的密钥(已经使用MD5加密好了)
        String hospSign = (String) paramMap.get("sign");

        // 3.获取医院设置表中的密钥并进行MD5加密
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);

        // 4.密钥不匹配就抛出错误
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 5.传递的图片涉及base64编码问题，需要将logoData记录中所有的" "替换为+
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        // 6.执行上传操作
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
