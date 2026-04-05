package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Auth;

import java.util.List;

public interface IAuthService {
    List<Auth> list();

    Auth get(String id);

    boolean create(Auth body);

    boolean update(Auth body);

    boolean delete(String id);
}

