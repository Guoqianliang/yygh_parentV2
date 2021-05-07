package com.gql.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: 远程调用service-cmn
 * @author Guoqianliang
 * @date 18:55 - 2021/4/22
 */
@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {
    /**
     * 根据dictCode和value查询 数据字典
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    /**
     * 根据value查询 数据字典
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
