package com.gql.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @Description: 读取excel的监听器
 * @author Guoqianliang
 * @date 20:02 - 2021/4/14
 */
public class ExcelListener extends AnalysisEventListener<UserData> {
    // 读取第一行表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息" + headMap);
    }

    // 从第二行开始一行一行读取excel内容,每行内容读取到userData中
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }

    // 读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完毕！");
    }
}
