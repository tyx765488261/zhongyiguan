package com.zhongyiguan.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhongyiguan.back.entity.Doctor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
    @Select("""
        <script>
        SELECT
            d.doctor_id AS doctorId,
            d.doctor_name AS doctorName,
            d.doctor_level AS doctorLevel,
            d.department_id AS departmentId,
            dep.department_name AS departmentName,
            pdep.department_id AS parentDepartmentId,
            pdep.department_name AS parentDepartmentName,
            il.illness_id AS illnessId,
            il.illness_name AS illnessName,
            d.doctor_exclusive_introduction AS doctorExclusiveIntroduction,
            d.doctor_exclusive_profession_introdction AS doctorExclusiveProfessionIntrodction,
            d.doctor_hospital_id AS doctorHospitalId,
            d.docotor_hospital_name AS doctorHospitalName,
            d.doctor_lever_name AS doctorLeverName,
            d.location_id AS locationId,
            d.location_name AS locationName
        FROM doctor d
        LEFT JOIN department dep ON dep.department_id = d.department_id
        LEFT JOIN department pdep ON pdep.department_id = dep.department_parent_id
        LEFT JOIN illness il ON il.department_id = d.department_id
        <where>
            <if test="departmentId != null and departmentId != ''">
                d.department_id = #{departmentId}
            </if>
            <if test="illnessId != null and illnessId != ''">
                AND il.illness_id = #{illnessId}
            </if>
            <if test="keyword != null and keyword != ''">
                AND (
                    d.doctor_name LIKE CONCAT('%', #{keyword}, '%')
                    OR dep.department_name LIKE CONCAT('%', #{keyword}, '%')
                    OR il.illness_name LIKE CONCAT('%', #{keyword}, '%')
                )
            </if>
            <if test="locationId != null and locationId != ''">
                AND d.location_id = #{locationId}
            </if>
            <if test="visitMode != null and visitMode.equals('online')">
                AND d.online = 1
            </if>
            <if test="visitMode != null and visitMode.equals('offline')">
                AND d.offline = 1
            </if>
        </where>
        ORDER BY d.doctor_id
        </script>
        """)
    List<Map<String, Object>> selectDoctorByCondition(@Param("departmentId") String departmentId,
                                                      @Param("illnessId") String illnessId,
                                                      @Param("keyword") String keyword,
                                                      @Param("locationId") String locationId,
                                                      @Param("visitMode") String visitMode);

    @Select("SELECT d.location_name AS locationName FROM doctor d WHERE d.location_id = #{locationId} LIMIT 1")
    String selectLocationNameByLocationId(@Param("locationId") String locationId);
}