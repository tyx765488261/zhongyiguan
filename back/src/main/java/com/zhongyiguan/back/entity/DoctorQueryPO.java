package com.zhongyiguan.back.entity;

import lombok.Data;

@Data
public class DoctorQueryPO {
    // 二级科室
    private String doctorId;
    private String doctorName;
    private String doctorLevel;
    private String departmentId;
    private String departmentName;
    // 一级科室（父科室）
    private String parentDepartmentId;
    private String parentDepartmentName;
    private String illnessId;
    private String illnessName;
    private String doctorExclusiveIntroduction;
    private String doctorExclusiveProfessionIntrodction;
    private String doctorHospitalId;
    private String doctorHospitalName;
    private String doctorLeverName;
    private String locationId;
    private String locationName;
}

