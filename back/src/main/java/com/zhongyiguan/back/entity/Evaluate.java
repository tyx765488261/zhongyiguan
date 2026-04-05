package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("evaluate")
public class Evaluate {
    @TableId(value = "evaluate_id", type = IdType.INPUT)
    private String evaluateId;

    @TableField("evaluate_text")
    private String evaluateText;

    @TableField("evaluate_doctor")
    private String evaluateDoctor;

    @TableField("evaluate_level")
    private String evaluateLevel;
}

