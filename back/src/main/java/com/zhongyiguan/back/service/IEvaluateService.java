package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Evaluate;

import java.util.List;

public interface IEvaluateService {
    List<Evaluate> list(String doctorId);

    Evaluate get(String id);

    boolean create(Evaluate body);

    boolean update(Evaluate body);

    boolean delete(String id);
}

