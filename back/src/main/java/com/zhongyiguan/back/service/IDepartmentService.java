package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> list();

    Department get(String id);

    boolean create(Department body);

    boolean update(Department body);

    boolean delete(String id);
}

