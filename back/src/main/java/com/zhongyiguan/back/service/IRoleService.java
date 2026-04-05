package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> list();

    Role get(String id);

    boolean create(Role body);

    boolean update(Role body);

    boolean delete(String id);
}

