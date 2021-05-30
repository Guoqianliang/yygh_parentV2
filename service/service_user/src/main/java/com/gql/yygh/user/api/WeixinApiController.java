package com.gql.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.gql.yygh.common.helper.JwtHelper;
import com.gql.yygh.common.result.Result;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.user.service.UserInfoService;
import com.gql.yygh.user.utils.ConstantWxPropertiesUtils;
import com.gql.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author Guoqianliang
 */
// Controller不会返回数据(更方便跳转),RestController默认返回JSON数据
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;


    // 微信扫码后回调的返回
    @GetMapping("callback")
    public String callback(String code, String state) {
        // 1: 获取临时票据
        System.out.println("code:" + code);
        // 2: 用code、微信id和密钥,请求微信固定地址,得到两个值access_token和openid
        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                // 设置id
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                // 设置密钥
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                // 设置值
                code);
        // 使用httpclient请求这个地址
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:" + accessTokenInfo);
            // 从返回字符串获取两个值 openid和access_token 将String类型转换为Json类型
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");
            System.out.println("access_token:" + access_token + ",openid:" + openid);
            // 判断数据库是否存在微信的扫描人信息
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            // 如果数据库中不存在微信的扫描人信息
            if (null == userInfo) {

                // 3: 用access_token、openid请求微信地址,得到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" + "?access_token=%s" + "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo:" + resultUserInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultUserInfo);
                // 获取用户的昵称和头像
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");

                // 4：将获取到的用户昵称添加到数据库中
                userInfo = new UserInfo();
                // 用户昵称
                userInfo.setNickName(nickname);
                // 微信openid
                userInfo.setOpenid(openid);
                // 状态为可用
                userInfo.setStatus(1);
                // 调用Service层添加到数据库
                userInfoService.save(userInfo);
            }

            // 5：返回name和token字符串
            Map<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            // 如果手机号为空,返回openid,如果不为空,就返回openid值为空的字符串
            // 前端判断:如果openid不为空,绑定手机号;如果openid为空,不需要绑定手机号
            if (StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            // 使用jwt,根据id和name生成token字符串,放入map
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            // 跳转到前端页面中


            return "redirect:" +
                    ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token=" + map.get("token") + "&openid="
                    + map.get("openid") + "&name=" + URLEncoder.encode((String) map.get("name"), "UTF-8");
            // , "utf-8"
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    // 1.生成微信扫描二维码
    // 返回生成二维码需要的参数
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {

        try {
            Map<String, Object> map = new HashMap<>();
            // 应用唯一标识
            map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);

            // 重定向地址,需要进行Url编码
            String redirectUri = null;
            redirectUri = URLEncoder.encode(ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL, "UTF-8");
            map.put("redirectUri", redirectUri);

            // 应用授权作用域,网页应用填写snsapi_login即可
            map.put("scope", "snsapi_login");
            // 当前时间
            map.put("state", System.currentTimeMillis() + "");

            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    // 2.回调的返回,得到扫描人信息


}
