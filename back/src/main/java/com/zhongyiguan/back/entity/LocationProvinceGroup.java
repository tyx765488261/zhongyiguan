package com.zhongyiguan.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 地点下拉：一省 + 下属地级市列表（供前端二级联动）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationProvinceGroup {
    private String provinceId;
    private String provinceName;
    private List<LocationOption> cities;
}
