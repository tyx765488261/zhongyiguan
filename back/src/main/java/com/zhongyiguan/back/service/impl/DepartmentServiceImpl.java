package com.zhongyiguan.back.service.impl;

import com.zhongyiguan.back.entity.Department;
import com.zhongyiguan.back.mapper.DepartmentMapper;
import com.zhongyiguan.back.service.IDepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> list() {
        return departmentMapper.selectList(null);
    }

    @Override
    public Department get(String id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public boolean create(Department body) {
        return departmentMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Department body) {
        return departmentMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return departmentMapper.deleteById(id) > 0;
    }
}

