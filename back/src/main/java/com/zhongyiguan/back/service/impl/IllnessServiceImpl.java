package com.zhongyiguan.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongyiguan.back.entity.Illness;
import com.zhongyiguan.back.mapper.IllnessMapper;
import com.zhongyiguan.back.service.IIllnessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IllnessServiceImpl implements IIllnessService {

    private final IllnessMapper illnessMapper;

    public IllnessServiceImpl(IllnessMapper illnessMapper) {
        this.illnessMapper = illnessMapper;
    }

    @Override
    public List<Illness> list(String departmentId) {
        if (departmentId == null || departmentId.isBlank()) {
            return illnessMapper.selectList(null);
        }
        return illnessMapper.selectList(
                new LambdaQueryWrapper<Illness>().eq(Illness::getDepartmentId, departmentId)
        );
    }

    @Override
    public Illness get(String id) {
        return illnessMapper.selectById(id);
    }

    @Override
    public boolean create(Illness body) {
        return illnessMapper.insert(body) > 0;
    }

    @Override
    public boolean update(Illness body) {
        return illnessMapper.updateById(body) > 0;
    }

    @Override
    public boolean delete(String id) {
        return illnessMapper.deleteById(id) > 0;
    }
}

