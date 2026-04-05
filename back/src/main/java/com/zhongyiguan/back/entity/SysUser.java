package com.zhongyiguan.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`user`")
public class SysUser {
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;

    @TableField("role_id")
    private String roleId;

    @TableField("account")
    private String account;

    @TableField("passport")
    private String passport;

    @TableField("wxid")
    private String wxid;

    @TableField("phone")
    private String phone;

    @TableField("work_id")
    private String workId;
}

