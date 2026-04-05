package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Doctor;
import com.zhongyiguan.back.entity.DoctorQueryPO;
import com.zhongyiguan.back.service.IDoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/doctor")
public class DoctorController {

    private final IDoctorService doctorService;

    public DoctorController(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/list")
    public List<Doctor> list(@RequestParam(value = "departmentId", required = false) String departmentId) {
        return doctorService.list(departmentId);
    }

    /**
     * 根据二级科室、疾病(illness)、关键词、地点(地级市 location_id) 查询医生。
     * visitMode：online 仅 online=1；offline 仅 offline=1；不传则不按该维度过滤。
     * /com/back/api/doctor/query?departmentId=&illnessId=&keyword=&locationId=&visitMode=online
     */
    @GetMapping("/query")
    public List<DoctorQueryPO> query(@RequestParam(value = "departmentId", required = false) String departmentId,
                                     @RequestParam(value = "illnessId", required = false) String illnessId,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "locationId", required = false) String locationId,
                                     @RequestParam(value = "visitMode", required = false) String visitMode,
                                     @RequestParam(value = "schemdulTime", required = false) String schemdulTime) {
        return doctorService.queryByCondition(departmentId, illnessId, keyword, locationId, visitMode,schemdulTime);
    }

    @GetMapping("/{id}")
    public Doctor get(@PathVariable("id") String id) {
        return doctorService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Doctor body) {
        return doctorService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Doctor body) {
        return doctorService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return doctorService.delete(id);
    }
}
