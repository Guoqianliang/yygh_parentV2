package com.gql.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gql.yygh.common.exception.YyghException;
import com.gql.yygh.common.result.Result;
import com.gql.yygh.common.utils.MD5;
import com.gql.yygh.hosp.service.HospitalSetService;
import com.gql.yygh.model.hosp.HospitalSet;
import com.gql.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @Description: Controller
 * @author Guoqianliang
 * @date 0:16 - 2021/4/4
 */

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    /**
     * 注入Service
     */
    @Autowired
    private HospitalSetService hospitalSetService;

    //============================1增=======================================

    /**
     * 1.增加记录
     * @param hospitalSet
     * @return
     */
    @ApiOperation(value = "saveHospitalSet 增加", notes = "")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态字段 (1:可以使用,0：不能使用)
        hospitalSet.setStatus(1);
        // 设置签名密钥字段(使用MD5加密)
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + new Random().nextInt(1000)));
        // 调用方法执行添加
        boolean flag = hospitalSetService.save(hospitalSet);

        return Result.isOKorFail(flag);
    }

    //============================2删=======================================

    /**
     * 2.1 逻辑删除记录
     * @param id
     * @return
     */
    @ApiOperation(value = "removeHospitalSet 逻辑删除", notes = "")
    @DeleteMapping(value = "{id}")
    public Result removeHospitalSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return Result.isOKorFail(flag);
    }

    /**
     * 2.2 批量删除记录
     * @param idList
     * @return
     */
    @ApiOperation(value = "batchRemoveHospitalSet 批量删除", notes = "")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }
    //============================3改=======================================

    /**
     * 3 修改记录
     * @param updateHospitalSet
     * @return
     */
    @ApiOperation(value = "updateHospitalSet 修改", notes = "")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet updateHospitalSet) {
        boolean flag = hospitalSetService.updateById(updateHospitalSet);
        return Result.isOKorFail(flag);
    }


    //============================4查=======================================

    /**
     * 4.1 查询所有记录
     * @return
     */
    @ApiOperation(value = "findAllHospitalSet 查询所有", notes = "")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 4.2 条件查询带分页
     * @param current
     * @param limit
     * @param hospitalSetQueryVo
     * @return
     */
    @ApiOperation(value = "findPageHospitalSet 条件查询(带分页)", notes = "")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospitalSet(@PathVariable Long current,
                                      @PathVariable Long limit,
                                      @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 1. 创建page对象。传递当前页、每页记录数
        Page<HospitalSet> page = new Page(current, limit);
        // 2. 创建QueryWrapper对象.
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        // 2.1 对医院名称和医院编号进行判空
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            queryWrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hoscode)) {
            queryWrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        // 3. 调用方法执行分页。(参数是上面两个对象)
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, queryWrapper);
        // 返回结果
        return Result.ok(pageHospitalSet);
    }

    /**
     * 4.3根据id获取记录
     * @param id
     * @return
     */
    @ApiOperation(value = "getHospitalSet 根据id查询", notes = "")
    @GetMapping("HospitalSet/{id}")
    public Result getHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }
    //============================5医院设置表锁定和解锁=======================================

    /**
     * 5.医院设置表锁定和解锁
     * @param id
     * @param status
     * @return
     */
    @ApiOperation(value = "lockHospitalSet 医院设置表锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置状态
        hospitalSet.setStatus(status);
        // 调用更新方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }
    //============================6发送签名密钥=======================================

    /**
     * 6.发送签名密钥
     * @param id
     * @return
     */
    @ApiOperation(value = "sendKeyHospitalSet 发送签名密匙")
    @PutMapping("sendKey/{id}")
    public Result sendKeyHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 签名密钥
        String signKey = hospitalSet.getSignKey();
        // 医院编号
        String hoscode = hospitalSet.getHoscode();
        // TODO 发送短信

        return Result.ok();
    }
}




















