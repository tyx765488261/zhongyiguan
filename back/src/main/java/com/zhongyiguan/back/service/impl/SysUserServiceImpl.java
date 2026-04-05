package com.zhongyiguan.back.service.impl;

import com.zhongyiguan.back.entity.SysUser;
import com.zhongyiguan.back.mapper.SysUserMapper;
import com.zhongyiguan.back.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserMapper sysUserMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public List<SysUser> list() {
        return sysUserMapper.selectList(null);
    }

    @Override
    public SysUser get(String id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public boolean create(SysUser body) {
        return sysUserMapper.insert(body) > 0;
    }

    @Override
    public boolean update(SysUser body) {
        return sysUserMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return sysUserMapper.deleteById(id) > 0;
    }
}

