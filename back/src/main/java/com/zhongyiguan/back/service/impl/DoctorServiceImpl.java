package com.zhongyiguan.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongyiguan.back.entity.Department;
import com.zhongyiguan.back.entity.Doctor;
import com.zhongyiguan.back.entity.DoctorQueryPO;
import com.zhongyiguan.back.mapper.DepartmentMapper;
import com.zhongyiguan.back.mapper.DoctorMapper;
import com.zhongyiguan.back.service.IDoctorService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorMapper doctorMapper;
    private final DepartmentMapper departmentMapper;

    public DoctorServiceImpl(DoctorMapper doctorMapper, DepartmentMapper departmentMapper) {
        this.doctorMapper = doctorMapper;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Doctor> list(String departmentId) {
        if (departmentId == null || departmentId.isBlank()) {
            return doctorMapper.selectList(null);
        }
        return doctorMapper.selectList(
                new LambdaQueryWrapper<Doctor>().eq(Doctor::getDepartmentId, departmentId)
        );
    }

    @Override
    public List<DoctorQueryPO> queryByCondition(String departmentId, String illnessId, String keyword,
                                                  String locationId, String visitMode) {
        // 科室页直接按二级科室进入医生列表时，不带 illnessId：
        // 这里直接走 doctor 单表查询，避免多表 left join 造成医生重复。
        if (illnessId == null || illnessId.isBlank()) {
            List<DoctorQueryPO> list = listByDepartmentAndLocation(departmentId, locationId, visitMode).stream()
                    .map(this::toDoctorQueryPO)
                    .toList();
            return deduplicateByDoctorId(list);
        }

        String visitModeSql = normalizeVisitModeForSql(visitMode);
        List<Map<String, Object>> rows =
                doctorMapper.selectDoctorByCondition(departmentId, illnessId, keyword, locationId, visitModeSql);
        List<DoctorQueryPO> list = rows.stream().map(this::toDoctorQueryPO).toList();
        return deduplicateByDoctorId(list);
    }

    private List<Doctor> listByDepartmentAndLocation(String departmentId, String locationId, String visitMode) {
        LambdaQueryWrapper<Doctor> w = new LambdaQueryWrapper<>();
        if (departmentId != null && !departmentId.isBlank()) {
            w.eq(Doctor::getDepartmentId, departmentId);
        }
        if (locationId != null && !locationId.isBlank()) {
            w.eq(Doctor::getLocationId, locationId);
        }
        applyVisitModeFilter(w, visitMode);
        return doctorMapper.selectList(w);
    }

    private static void applyVisitModeFilter(LambdaQueryWrapper<Doctor> w, String visitMode) {
        String m = normalizeVisitModeForSql(visitMode);
        if ("online".equals(m)) {
            w.eq(Doctor::getOnline, true);
        } else if ("offline".equals(m)) {
            w.eq(Doctor::getOffline, true);
        }
    }

    /** 供 MyBatis 动态 SQL 与 Wrapper 共用，仅返回 online / offline 或 null */
    private static String normalizeVisitModeForSql(String visitMode) {
        if (visitMode == null || visitMode.isBlank()) {
            return null;
        }
        String m = visitMode.trim().toLowerCase();
        if ("online".equals(m)) {
            return "online";
        }
        if ("offline".equals(m)) {
            return "offline";
        }
        return null;
    }

    @Override
    public Doctor get(String id) {
        return doctorMapper.selectById(id);
    }

    @Override
    public boolean create(Doctor body) {
        return doctorMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Doctor body) {
        return doctorMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return doctorMapper.deleteById(id) > 0;
    }

    private DoctorQueryPO toDoctorQueryPO(Map<String, Object> row) {
        DoctorQueryPO po = new DoctorQueryPO();
        po.setDoctorId(stringValue(row.get("doctorId")));
        po.setDoctorName(stringValue(row.get("doctorName")));
        po.setDoctorLevel(stringValue(row.get("doctorLevel")));
        po.setDepartmentId(stringValue(row.get("departmentId")));
        po.setDepartmentName(stringValue(row.get("departmentName")));
        po.setParentDepartmentId(stringValue(row.get("parentDepartmentId")));
        po.setParentDepartmentName(stringValue(row.get("parentDepartmentName")));
        po.setIllnessId(stringValue(row.get("illnessId")));
        po.setIllnessName(stringValue(row.get("illnessName")));
        po.setDoctorExclusiveIntroduction(stringValue(row.get("doctorExclusiveIntroduction")));
        po.setDoctorExclusiveProfessionIntrodction(stringValue(row.get("doctorExclusiveProfessionIntrodction")));
        po.setDoctorHospitalId(stringValue(row.get("doctorHospitalId")));
        po.setDoctorHospitalName(stringValue(row.get("doctorHospitalName")));
        po.setDoctorLeverName(stringValue(row.get("doctorLeverName")));
        po.setLocationId(stringValue(row.get("locationId")));
        po.setLocationName(stringValue(row.get("locationName")));
        return po;
    }

    private DoctorQueryPO toDoctorQueryPO(Doctor d) {
        DoctorQueryPO po = new DoctorQueryPO();
        po.setDoctorId(stringValue(d.getDoctorId()));
        po.setDoctorName(stringValue(d.getDoctorName()));
        po.setDoctorLevel(stringValue(d.getDoctorLevel()));
        po.setDepartmentId(stringValue(d.getDepartmentId()));
        fillParentDepartment(po, d.getDepartmentId());
        po.setDoctorExclusiveIntroduction(stringValue(d.getDoctorExclusiveIntroduction()));
        po.setDoctorExclusiveProfessionIntrodction(stringValue(d.getDoctorExclusiveProfessionIntrodction()));
        po.setDoctorHospitalId(stringValue(d.getDoctorHospitalId()));
        po.setDoctorHospitalName(stringValue(d.getDocotorHospitalName()));
        po.setDoctorLeverName(stringValue(d.getDoctorLeverName()));
        po.setLocationId(stringValue(d.getLocationId()));
        po.setLocationName(stringValue(d.getLocationName()));
        return po;
    }

    private void fillParentDepartment(DoctorQueryPO po, String childDepartmentId) {
        if (childDepartmentId == null || childDepartmentId.isBlank()) return;
        Department child = departmentMapper.selectById(childDepartmentId);
        if (child == null) return;
        po.setDepartmentName(stringValue(child.getDepartmentName()));
        String parentId = stringValue(child.getDepartmentParentId());
        po.setParentDepartmentId(parentId);
        if (parentId.isBlank() || "0".equals(parentId)) return;
        Department parent = departmentMapper.selectById(parentId);
        if (parent != null) {
            po.setParentDepartmentName(stringValue(parent.getDepartmentName()));
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private List<DoctorQueryPO> deduplicateByDoctorId(List<DoctorQueryPO> source) {
        Map<String, DoctorQueryPO> dedup = new LinkedHashMap<>();
        for (DoctorQueryPO po : source) {
            String id = stringValue(po.getDoctorId());
            if (id.isBlank()) continue;
            // keep first row order
            dedup.putIfAbsent(id, po);
        }
        return dedup.values().stream().toList();
    }
}

