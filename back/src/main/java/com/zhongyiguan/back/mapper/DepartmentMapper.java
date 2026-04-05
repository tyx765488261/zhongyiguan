package com.zhongyiguan.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhongyiguan.back.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}