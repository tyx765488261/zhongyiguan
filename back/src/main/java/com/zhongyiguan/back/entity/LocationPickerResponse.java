package com.zhongyiguan.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 地区二级联动：一次返回全省市树 + 可选的当前选中回显（避免前端再请求 context）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationPickerResponse {
    private List<LocationProvinceGroup> provinces;
    /** 根据请求参数 locationId 解析；未传或无效时各字段为空字符串 */
    private RegionContext selection;
}
