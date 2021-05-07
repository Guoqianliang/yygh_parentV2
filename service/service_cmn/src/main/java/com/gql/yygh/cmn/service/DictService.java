package com.gql.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gql.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Description: Service接口
 * @author Guoqianliang
 * @date 0:10 - 2021/4/4
 */
public interface DictService extends IService<Dict> {

    /**
     * 根据id查询子数据列表
     * @param id
     * @return list
     */
    List<Dict> findChildData(Long id);

    /**
     * 导出数据字典到Excel
     * @param response
     */
    void exportDictData(HttpServletResponse response);

    /**
     * 导入数据字典到网页
     * @param file
     */
    void importDictData(MultipartFile file);

    /**
     * 根据dictCode和value查询 数据字典
     * @param dictCode
     * @param value
     * @return
     */
    String getDictName(String dictCode, String value);

    /**
     * 根据dictCode获取下级结点
     * @param dictCode
     * @return
     */
    List<Dict> findByDictCode(String dictCode);
}
