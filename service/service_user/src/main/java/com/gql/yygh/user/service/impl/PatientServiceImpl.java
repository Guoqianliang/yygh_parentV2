package com.gql.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gql.yygh.cmn.client.DictFeignClient;
import com.gql.yygh.enums.DictEnum;
import com.gql.yygh.model.user.Patient;
import com.gql.yygh.model.user.UserInfo;
import com.gql.yygh.user.mapper.PatientMapper;
import com.gql.yygh.user.mapper.UserInfoMapper;
import com.gql.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author Guoqianliang
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
        implements PatientService {
    @Autowired
    private DictFeignClient dictFeignClient;

    // 获取就诊人列表
    @Override
    public List<Patient> findAllUserId(Long userId) {
        // 根据userId查询所有就诊人信息列表
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Patient> patientList = baseMapper.selectList(queryWrapper);
        // 通过Feign调用数据字典表,得到编号对应的内容
        patientList.stream().forEach(item -> {
            // 其他参数的封装
            this.packPatient(item);
        });
        return patientList;
    }

    // 根据id获取就诊人信息
    @Override
    public Patient getPatientId(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    // 其他参数的封装
    private void packPatient(Patient patient) {
        // 根据证件类型编码获取证件具体值
        String certificatesTypeString = dictFeignClient.getName(
                DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        // 联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        // 省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        // 市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        // 区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        // 将获取到的值都放入patient中
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
    }
}
