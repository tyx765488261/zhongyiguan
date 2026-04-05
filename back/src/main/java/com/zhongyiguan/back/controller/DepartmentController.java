package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.Department;
import com.zhongyiguan.back.service.IDepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/department")
public class DepartmentController {

    private final IDepartmentService departmentService;

    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public List<Department> list() {
        return departmentService.list();
    }

    @GetMapping("/{id}")
    public Department get(@PathVariable("id") String id) {
        return departmentService.get(id);
    }

    @PostMapping
    public boolean create(@RequestBody Department body) {
        return departmentService.create(body);
    }

    @PutMapping
    public boolean update(@RequestBody Department body) {
        return departmentService.update(body);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id) {
        return departmentService.delete(id);
    }
}
