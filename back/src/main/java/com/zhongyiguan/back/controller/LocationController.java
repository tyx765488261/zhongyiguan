package com.zhongyiguan.back.controller;

import com.zhongyiguan.back.entity.LocationOption;
import com.zhongyiguan.back.entity.LocationPickerResponse;
import com.zhongyiguan.back.entity.LocationProvinceGroup;
import com.zhongyiguan.back.entity.RegionContext;
import com.zhongyiguan.back.service.ILocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/com/back/api/location")
public class LocationController {

    private final ILocationService locationService;

    public LocationController(ILocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * 地点下拉完整列表（省 + 地级市），数据全部来自 locations 表；选地区后再带 locationId 查医生。
     */
    @GetMapping("/list")
    public List<LocationProvinceGroup> list() {
        return locationService.listPickerTree();
    }

    /**
     * 地区二级联动（推荐）：一次返回全省市树 + 根据 locationId 解析的回显字段，前端无需再调 /context。
     */
    @GetMapping("/picker")
    public LocationPickerResponse picker(@RequestParam(value = "locationId", required = false) String locationId) {
        return locationService.buildPickerResponse(locationId);
    }

    /** 省级列表（不含「全国」） */
    @GetMapping("/provinces")
    public List<LocationOption> provinces() {
        return locationService.listProvinces();
    }

    /** 省下地级市（doctor.location_id）；直辖市为省本级一条 */
    @GetMapping("/cities")
    public List<LocationOption> cities(@RequestParam("parentId") String parentId) {
        return locationService.listPrefectureCities(parentId);
    }

    /** 根据已选地级市 id 回显省、市名称 */
    @GetMapping("/context")
    public RegionContext context(@RequestParam(value = "locationId", required = false) String locationId) {
        return locationService.resolveContext(locationId);
    }
}
