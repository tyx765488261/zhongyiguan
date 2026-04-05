package com.zhongyiguan.back.service.impl;

import com.zhongyiguan.back.entity.Role;
import com.zhongyiguan.back.mapper.RoleMapper;
import com.zhongyiguan.back.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public List<Role> list() {
        return roleMapper.selectList(null);
    }

    @Override
    public Role get(String id) {
        return roleMapper.selectById(id);
    }

    @Override
    public boolean create(Role body) {
        return roleMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Role body) {
        return roleMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return roleMapper.deleteById(id) > 0;
    }
}

