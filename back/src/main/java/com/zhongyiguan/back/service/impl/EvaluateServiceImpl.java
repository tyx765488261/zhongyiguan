package com.zhongyiguan.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongyiguan.back.entity.Evaluate;
import com.zhongyiguan.back.mapper.EvaluateMapper;
import com.zhongyiguan.back.service.IEvaluateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluateServiceImpl implements IEvaluateService {

    private final EvaluateMapper evaluateMapper;

    public EvaluateServiceImpl(EvaluateMapper evaluateMapper) {
        this.evaluateMapper = evaluateMapper;
    }

    @Override
    public List<Evaluate> list(String doctorId) {
        if (doctorId == null || doctorId.isBlank()) {
            return evaluateMapper.selectList(null);
        }
        return evaluateMapper.selectList(
                new LambdaQueryWrapper<Evaluate>().eq(Evaluate::getEvaluateDoctor, doctorId)
        );
    }

    @Override
    public Evaluate get(String id) {
        return evaluateMapper.selectById(id);
    }

    @Override
    public boolean create(Evaluate body) {
        return evaluateMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Evaluate body) {
        return evaluateMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return evaluateMapper.deleteById(id) > 0;
    }
}

