package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Illness;

import java.util.List;

public interface IIllnessService {
    List<Illness> list(String departmentId);

    Illness get(String id);

    boolean create(Illness body);

    boolean update(Illness body);

    boolean delete(String id);
}

