package com.gql.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @author Guoqianliang
 */
public interface FileService {
    // 上传文件到阿里云oss
    String upload(MultipartFile file);
}
