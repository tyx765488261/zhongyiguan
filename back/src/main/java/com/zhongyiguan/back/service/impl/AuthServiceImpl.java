package com.zhongyiguan.back.service.impl;

import com.zhongyiguan.back.entity.Auth;
import com.zhongyiguan.back.mapper.AuthMapper;
import com.zhongyiguan.back.service.IAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthMapper authMapper;

    public AuthServiceImpl(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Override
    public List<Auth> list() {
        return authMapper.selectList(null);
    }

    @Override
    public Auth get(String id) {
        return authMapper.selectById(id);
    }

    @Override
    public boolean create(Auth body) {
        return authMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Auth body) {
        return authMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return authMapper.deleteById(id) > 0;
    }
}

