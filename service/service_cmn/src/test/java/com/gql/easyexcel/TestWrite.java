package com.gql.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 使用EasyExcel将数据写入Excel
 * @author Guoqianliang
 * @date 19:50 - 2021/4/14
 */
public class TestWrite {
    public static void main(String[] args) {
        // 向list集合添加100条数据
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            UserData data = new UserData();
            data.setId(i);
            data.setUsername("Hudie" + i + "号");
            data.setGender("男");
            data.setSalary(100000.00);
            list.add(data);
        }

        // 设置excel文件路径和文件名称
        String fileName = "D:\\Program Files (x86)\\test\\01.xlsx";

        // write方法实现写操作
        EasyExcel.write(fileName, UserData.class).sheet("用户信息")
                .doWrite(list);
    }
}
