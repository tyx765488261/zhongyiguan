package com.zhongyiguan.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongyiguan.back.entity.Schedul;
import com.zhongyiguan.back.mapper.SchedulMapper;
import com.zhongyiguan.back.service.ISchedulService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulServiceImpl implements ISchedulService {

    private final SchedulMapper schedulMapper;

    public SchedulServiceImpl(SchedulMapper schedulMapper) {
        this.schedulMapper = schedulMapper;
    }

    @Override
    public List<Schedul> list(String doctorId) {
        if (doctorId == null || doctorId.isBlank()) {
            return schedulMapper.selectList(null);
        }
        return schedulMapper.selectList(
                new LambdaQueryWrapper<Schedul>().eq(Schedul::getDoctorId, doctorId)
        );
    }

    @Override
    public Schedul get(String id) {
        return schedulMapper.selectById(id);
    }

    @Override
    public boolean create(Schedul body) {
        return schedulMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Schedul body) {
        return schedulMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return schedulMapper.deleteById(id) > 0;
    }
}

