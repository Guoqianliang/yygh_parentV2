package com.gql.yygh.hosp.repository;

import com.gql.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description: 排班-数据访问层
 * @author Guoqianliang
 * @date 10:11 - 2021/4/22
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    /**
     * 根据 医院编号 和 排班编号 查询科室信息
     * @param hoscode
     * @param hosScheduleId
     * @return
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号、科室编号、工作日期,查询排班详细信息.
     * @param hoscode
     * @param depcode
     * @param toDate
     * @return
     */
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
