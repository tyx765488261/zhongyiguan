package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("doctor")
public class Doctor {
    @TableId(value = "doctor_id", type = IdType.INPUT)
    private String doctorId;

    @TableField("doctor_name")
    private String doctorName;

    @TableField("doctor_level")
    private String doctorLevel;

    @TableField("department_id")
    private String departmentId;

    @TableField("doctor_hospital_id")
    private String doctorHospitalId;

    @TableField("docotor_hospital_name")
    private String docotorHospitalName;

    @TableField("doctor_exclusive_introduction")
    private String doctorExclusiveIntroduction;

    @TableField("doctor_exclusive_profession_introdction")
    private String doctorExclusiveProfessionIntrodction;

    @TableField("location_id")
    private String locationId;

    @TableField("location_name")
    private String locationName;

    @TableField("doctor_lever_name")
    private String doctorLeverName;

    @TableField("id_card")
    private String idCard;

    @TableField("birth_day")
    private String birthDay;

    @TableField("phone")
    private String phone;

    /** 是否支持在线问诊（图文咨询等） */
    @TableField("online")
    private Boolean online;

    /** 是否支持预约挂号（线下排班等） */
    @TableField("offline")
    private Boolean offline;
}
