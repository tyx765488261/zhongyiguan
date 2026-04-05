package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.Schedul;

import java.util.List;

public interface ISchedulService {
    List<Schedul> list(String doctorId);

    Schedul get(String id);

    boolean create(Schedul body);

    boolean update(Schedul body);

    boolean delete(String id);
}

