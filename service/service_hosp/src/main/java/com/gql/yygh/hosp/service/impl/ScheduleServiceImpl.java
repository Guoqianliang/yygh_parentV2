package com.gql.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gql.yygh.hosp.repository.ScheduleRepository;
import com.gql.yygh.hosp.service.DepartmentService;
import com.gql.yygh.hosp.service.HospitalService;
import com.gql.yygh.hosp.service.ScheduleService;
import com.gql.yygh.model.hosp.HospitalSet;
import com.gql.yygh.model.hosp.Schedule;
import com.gql.yygh.vo.hosp.BookingScheduleRuleVo;
import com.gql.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 排班
 * @author Guoqianliang
 * @date 10:13 - 2021/4/22
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 上传排班
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        // 1.把paramMap集合转换为Department对象(借助JSONObject工具)
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);
        // 2.根据 医院编号 和 排班编号 查询科室信息
        Schedule scheduleExist = scheduleRepository
                .getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        // 3.如果有就执行更新,没有就执行保存
        if (null != scheduleExist) {// 更新
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        } else {// 保存
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    /**
     * 查询排班
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        // 创建Pageable对象,设置当前页和每页记录数
        PageRequest pageable = PageRequest.of(page - 1, limit);
        // 创建Example对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);

        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    /**
     * 删除排班
     * @param hoscode
     * @param hosScheduleId
     */
    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (null != schedule) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    /**
     * 根据医院编号和科室编号,查询排班规则数据
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        // 1.根据医院编号 和 科室编号 查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        // 2.根据工作日workDate进行分组
        Aggregation agg = Aggregation.newAggregation(
                // 匹配条件
                Aggregation.match(criteria),
                // 分组字段
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        // 3.统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // 排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                // 4.实现分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        // 调用方法
        AggregationResults<BookingScheduleRuleVo> aggResults =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();

        // 分组查询后的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.group("workDate"));
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        // 获取日期对应的星期
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        // 设置最终数据，进行返回
        HashMap<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);
        // 根据编号获取医院名
        String hosName = hospitalService.getHospName(hoscode);
        // 其他基础数据
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);
        return result;
    }

    // 根据医院编号、科室编号、工作日期,查询排班详细信息.
    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        // 根据参数查询MongoDB得到List集合
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,
                depcode, new DateTime(workDate).toDate());
        // 遍历得到的List集合,向其中设置其他值:医院名称、科室名称、日期对应的星期
        scheduleList.stream().forEach(item -> {
            this.packSchedule(item);
        });

        return scheduleList;
    }

    // 封装排班详情的其他值
    private void packSchedule(Schedule schedule) {
        // 设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHospName(schedule.getHoscode()));
        // 设置科室名称
        schedule.getParam().put("depname", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        // 设置日期对应星期
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }

    /**
     * 根据日期获取对应星期
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }
}
