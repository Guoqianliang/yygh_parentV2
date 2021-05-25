package com.gql.yygh.common.utils;


import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description: 图片base64工具类
 * @author Guoqianliang
 */
public class ImageBase64Util {
    public static void main(String[] args) {
        String imageFile = "D:\\gql_Life\\Pictures\\hnmyjjxy.jpg";// 待处理的图片
        System.out.println(getImageString(imageFile));
    }

    public static String getImageString(String imageFile) {
        InputStream is = null;
        try {
            byte[] data = null;
            is = new FileInputStream(new File(imageFile));
            data = new byte[is.available()];
            is.read(data);
            return new String(Base64.encodeBase64(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                    is = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
