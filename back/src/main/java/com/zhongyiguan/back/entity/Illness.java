package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("illness")
public class Illness {
    @TableId(value = "illness_id", type = IdType.INPUT)
    private String illnessId;

    @TableField("illness_name")
    private String illnessName;

    @TableField("department_id")
    private String departmentId;
}

