package com.gql.yygh.msm.controller;

import com.gql.yygh.common.result.Result;
import com.gql.yygh.msm.service.MsmService;
import com.gql.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @author Guoqianliang
 */
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    // 发送手机验证码
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        // 从redis获取手机验证码,如果获取到就返回ok
        // (key:手机号,value:验证码)
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        // 如果获取不到,生成6位验证码
        code = RandomUtil.getSixBitRandom();
        // 偷偷打印到控制台
        System.out.println(code);
        // 调用service返回,通过整合短信服务进行发送
        boolean isSend = msmService.send(phone, code);

        // 将生成的验证码放入redis中,并设置有效时间
        if (isSend) {
            // 验证码超过1分钟失效
            redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("发送短信失败");
        }
    }
}

















