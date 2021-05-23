package com.gql.yygh.oss.controller;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @author Guoqianliang
 */
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;

    // 上传文件到阿里云oss
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        // 获取上传文件
        String url = fileService.upload(file);
        // 返回上传到的url路径
        return Result.ok(url);
    }
}
