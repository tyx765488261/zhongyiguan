package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department")
public class Department {
    @TableId(value = "department_id", type = IdType.INPUT)
    private String departmentId;

    @TableField("department_name")
    private String departmentName;

    @TableField("department_parent_id")
    private String departmentParentId;
}
