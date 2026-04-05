package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("auth")
public class Auth {
    @TableId(value = "auth_id", type = IdType.INPUT)
    private String authId;

    @TableField("auth_name")
    private String authName;

    @TableField("auth_privilege")
    private String authPrivilege;
}

