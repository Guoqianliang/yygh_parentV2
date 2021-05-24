package com.gql.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.gql.yygh.oss.service.FileService;
import com.gql.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Description:
 * @author Guoqianliang
 */
@Service
public class FileServiceImpl implements FileService {
    // 上传文件到阿里云oss
    @Override
    public String upload(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantOssPropertiesUtils.Endpoint;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传流文件
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            // 生成随机唯一值,使用uuid,添加到文件名称中
            String uuid = UUID.randomUUID().toString().replace("-", "");
            // uuid01.jpg
            fileName = uuid + fileName;
            // 按照当前日期,创建文件夹,上传到创建文件夹里面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;

            // 调用方法实现上传
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            // 返回上传之后的路径
            // https://hudie-crowd.oss-cn-beijing.aliyuncs.com/%E5%B0%8F%E8%93%9D%E5%A4%B4.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
