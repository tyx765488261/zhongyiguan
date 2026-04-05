package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Doctor;
import com.zhongyiguan.back.entity.DoctorQueryPO;

import java.util.List;

public interface IDoctorService {
    List<Doctor> list(String departmentId);

    List<DoctorQueryPO> queryByCondition(String departmentId, String illnessId, String keyword, String locationId,
                                         String visitMode,String schemdulTime);

    Doctor get(String id);

    boolean create(Doctor body);

    boolean update(Doctor body);

    boolean delete(String id);
}

