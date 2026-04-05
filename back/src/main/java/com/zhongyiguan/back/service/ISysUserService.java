package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.SysUser;

import java.util.List;

public interface ISysUserService {
    List<SysUser> list();

    SysUser get(String id);

    boolean create(SysUser body);

    boolean update(SysUser body);

    boolean delete(String id);
}

