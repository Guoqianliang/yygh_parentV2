package com.gql.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description: 创建excel对应的实体对象
 * @author Guoqianliang
 * @date 19:47 - 2021/4/14
 */
@Data
public class UserData {
    @ExcelProperty(value = "用户编号", index = 0)
    private int id;
    @ExcelProperty(value = "用户名", index = 1)
    private String username;
    @ExcelProperty(value = "性别", index = 2)
    private String gender;
    @ExcelProperty(value = "工资", index = 3)
    private Double salary;
}
