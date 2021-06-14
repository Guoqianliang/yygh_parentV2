package com.gql.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gql.yygh.cmn.listener.DictListener;
import com.gql.yygh.cmn.mapper.DictMapper;
import com.gql.yygh.cmn.service.DictService;
import com.gql.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.vo.cmn.DictVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Service实现类
 * @author Guoqianliang
 * @date 0:12 - 2021/4/4
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 根据id查询子数据列表
     * @param id
     * @return list
     */
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        for (Dict dict : dictList) {
            // 得到每一条记录的id值
            Long dictId = dict.getId();
            // 调用hasChildren方法判断是否包含子节点
            boolean flag = this.hasChildren(dictId);
            // 为每条记录设置hasChildren属性
            dict.setHasChildren(flag);
        }
        return dictList;
    }

    /**
     * 导出数据字典到Excel
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {

        try {
            // 设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = null;
            fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
            // 查询数据库
            List<Dict> dictList = baseMapper.selectList(null);

            // 将Dict转换为DictVo
            List<DictVo> dictVoList = new ArrayList<>();
            for (Dict dict : dictList) {
                DictVo dictVo = new DictVo();
                // 将dict中的值复制到dictVo中
                BeanUtils.copyProperties(dict, dictVo);
                dictVoList.add(dictVo);
            }
            // 调用writer方法进行写操作
            EasyExcel.write(response.getOutputStream(), DictVo.class).sheet("数据字典")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据字典到网页
     * @param file
     */
    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据dictCode和value查询 数据字典
     * @param dictCode
     * @param value
     * @return
     */
    @Override
    public String getDictName(String dictCode, String value) {
        // 如果dictCode为空,直接根据value查询;否则根据dictCode和value查询

        if(StringUtils.isEmpty(dictCode)) {
            // 直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            // 根据dictcode查询dict对象，得到dict的id值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            // 根据parent_id和value进行查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    /**
     * 根据dictCode获取下级结点
     * @param dictCode
     * @return
     */
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取对应id
        Long id = this.getDictByDictCode(dictCode).getId();
        // 根据id获取子结点
        List<Dict> childData = this.findChildData(id);
        return childData;
    }

    /**
     * 根据dict_code查询数据字典
     * @param dictCode
     * @return
     */
    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    /**
     * 判断id下面是否有子结点
     * @param id
     * @return true:有子结点,false:无子结点
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}

























