package com.gql.yygh.user.client;

import com.gql.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: 远程调用service-user
 * @author Guoqianliang
 */

@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {
    // 获取就诊人信息,根据id获取
    @GetMapping("/api/user/patient/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id);

}
