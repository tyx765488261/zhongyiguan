package com.zhongyiguan.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地区二级联动回显（省 + 地级市，与 doctor.location_id 一致为地级市编码）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionContext {
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
}
