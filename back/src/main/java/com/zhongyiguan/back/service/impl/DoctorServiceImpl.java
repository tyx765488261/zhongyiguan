package com.zhongyiguan.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongyiguan.back.entity.Department;
import com.zhongyiguan.back.entity.Doctor;
import com.zhongyiguan.back.entity.DoctorQueryPO;
import com.zhongyiguan.back.entity.Schedul;
import com.zhongyiguan.back.mapper.DepartmentMapper;
import com.zhongyiguan.back.mapper.DoctorMapper;
import com.zhongyiguan.back.mapper.SchedulMapper;
import com.zhongyiguan.back.service.IDoctorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements IDoctorService {

    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private SchedulMapper schedulMapper;

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
                                                  String locationId, String visitMode,String schemdulTime) {
        List<Doctor> list = listByDepartmentAndLocation(departmentId, keyword, locationId, visitMode);
        if(schemdulTime!=null){
            LambdaQueryWrapper<Schedul> wrapper = new LambdaQueryWrapper<>();
            List<LocalDateTime> datalist = getData(schemdulTime);
            wrapper.ge(Schedul::getStartTime, datalist.get(0))   // >= 当天开始
                    .lt(Schedul::getStartTime, datalist.get(1));    // < 第二天开始

            List<Schedul> schedulList = schedulMapper.selectList(wrapper);
            Map<String, String> map = schedulList.stream().collect(Collectors.toMap(Schedul::getDoctorId, Schedul::getDoctorId));
            list = list.stream().filter(doctor -> map.containsKey(doctor.getDoctorId())).toList();
        }
        List<DoctorQueryPO> polist = list.stream().map(this::toDoctorQueryPO).toList();
        return polist;
    }

    private List<LocalDateTime> getData(String dateStr){
        LocalDate date = LocalDate.parse(dateStr);
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        ZonedDateTime shanghaiStart = date.atStartOfDay(shanghaiZone);
        ZonedDateTime shanghaiEnd = date.plusDays(1).atStartOfDay(shanghaiZone);

// 转换为 UTC
        LocalDateTime utcStart = shanghaiStart.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime utcEnd = shanghaiEnd.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<LocalDateTime> list = new ArrayList<>();
        list.add(utcStart);
        list.add(utcEnd);
        return list;
    }

    private List<Doctor> listByDepartmentAndLocation(String departmentId, String keyword, String locationId, String visitMode) {
        LambdaQueryWrapper<Doctor> w = new LambdaQueryWrapper<>();
        if (departmentId != null && !departmentId.isBlank()) {
            w.eq(Doctor::getDepartmentId, departmentId);
        }
        if (locationId != null && !locationId.isBlank()) {
            w.eq(Doctor::getLocationId, locationId);
        }
        if (visitMode != null && !visitMode.isBlank()) {
            if(visitMode.equals("online")){
                w.eq(Doctor::getOnline, "true");
            }else if(visitMode.equals("offline")){
                w.eq(Doctor::getOffline, "true");
            }
        }
        if (keyword != null && !keyword.isBlank()) {
            w.like(Doctor::getDoctorName, keyword);
        }
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

