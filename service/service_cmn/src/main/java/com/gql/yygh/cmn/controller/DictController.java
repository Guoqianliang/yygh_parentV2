package com.gql.yygh.cmn.controller;

import com.gql.yygh.cmn.service.DictService;
import com.gql.yygh.common.result.Result;
import com.gql.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description: Controller
 * @author Guoqianliang
 * @date 11:04 - 2021/4/13
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 根据dictCode获取下级结点
     * @param dictCode
     * @return
     */
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    /**
     * 根据dictCode和value查询 数据字典
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    /**
     * 根据value查询 数据字典
     * @param value
     * @return
     */
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        String dictName = dictService.getDictName("", value);
        return dictName;
    }

    @ApiOperation(value = "导入数据字典到网页")
    @PostMapping("importData")
    public Result importDictData(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    @ApiOperation(value = "导出数据字典到Excel")
    @GetMapping("exportData")
    public void exportDictData(HttpServletResponse response) throws IOException {
        dictService.exportDictData(response);
    }

    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }
}
