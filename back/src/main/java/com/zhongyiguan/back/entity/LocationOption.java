package com.zhongyiguan.back.entity;

import lombok.Data;

/**
 * 地区筛选项（locations 表 / 与 doctor.location_id、location_name 对应）
 */
@Data
public class LocationOption {
    private String locationId;
    private String locationName;
}
