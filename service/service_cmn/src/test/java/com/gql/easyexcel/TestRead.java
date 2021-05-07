package com.gql.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * @Description: EasyExcel读出excel技术演示
 * @author Guoqianliang
 * @date 20:08 - 2021/4/14
 */
public class TestRead {
    public static void main(String[] args) {
        // 要读取文件的路径
        String fileName = "D:\\Program Files (x86)\\test\\01.xlsx";
        // 调用read方法实现读取操作
        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet("用户信息").doRead();
    }
}
