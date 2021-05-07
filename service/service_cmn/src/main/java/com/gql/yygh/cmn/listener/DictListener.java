package com.gql.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.gql.yygh.cmn.mapper.DictMapper;
import com.gql.yygh.model.cmn.Dict;
import com.gql.yygh.vo.cmn.DictVo;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

/**
 * @Description: 上传数据字典时需要的监听器
 * @author Guoqianliang
 * @date 11:15 - 2021/4/15
 */
public class DictListener extends AnalysisEventListener<DictVo> {
    // 调用Dao
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    // 读取Excel内容
    @Override
    public void invoke(DictVo DictVo, AnalysisContext context) {
        // 将DictVO对象复制到Dict中
        Dict dict = new Dict();
        BeanUtils.copyProperties(DictVo, dict);
        // 将数据添加到数据库
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
