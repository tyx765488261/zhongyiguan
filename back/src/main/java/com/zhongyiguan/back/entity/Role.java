package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    @TableId(value = "role_id", type = IdType.INPUT)
    private String roleId;

    @TableField("role_name")
    private String roleName;

    @TableField("auth_id")
    private String authId;
}

