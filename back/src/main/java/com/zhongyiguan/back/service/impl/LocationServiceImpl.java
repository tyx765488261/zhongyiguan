package com.zhongyiguan.back.service.impl;

import com.zhongyiguan.back.entity.LocationOption;
import com.zhongyiguan.back.entity.LocationPickerResponse;
import com.zhongyiguan.back.entity.LocationProvinceGroup;
import com.zhongyiguan.back.entity.RegionContext;
import com.zhongyiguan.back.mapper.DoctorMapper;
import com.zhongyiguan.back.mapper.LocationMapper;
import com.zhongyiguan.back.service.ILocationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationServiceImpl implements ILocationService {

    /** 直辖市：医生表 location_id 存省本级编码（地级市语义为「全市」） */
    private static final Set<String> MUNICIPALITY_IDS = Set.of("110000", "120000", "310000", "500000");

    private final LocationMapper locationMapper;
    private final DoctorMapper doctorMapper;

    public LocationServiceImpl(LocationMapper locationMapper, DoctorMapper doctorMapper) {
        this.locationMapper = locationMapper;
        this.doctorMapper = doctorMapper;
    }

    /**
     * 仅来自 locations 字典的完整省/地级市树，不按医生数据裁剪。
     * 字典无数据或异常时返回空列表（前端仍可展示「全国」）。
     */
    @Override
    public List<LocationProvinceGroup> listPickerTree() {
        try {
            List<Map<String, Object>> provRows = locationMapper.selectProvinces();
            if (provRows == null || provRows.isEmpty()) {
                return List.of();
            }
            List<LocationProvinceGroup> out = new ArrayList<>();
            for (Map<String, Object> row : provRows) {
                LocationOption p = toOption(row);
                if (p.getLocationId().isBlank()) {
                    continue;
                }
                List<LocationOption> cities = listPrefectureCities(p.getLocationId());
                out.add(new LocationProvinceGroup(p.getLocationId(), p.getLocationName(), cities));
            }
            return out;
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public LocationPickerResponse buildPickerResponse(String locationId) {
        List<LocationProvinceGroup> provinces = listPickerTree();
        String lid = locationId == null ? "" : locationId.trim();
        RegionContext selection = resolveContext(lid);
        return new LocationPickerResponse(provinces, selection);
    }

    @Override
    public List<LocationOption> listProvinces() {
        return locationMapper.selectProvinces().stream().map(this::toOption).toList();
    }

    @Override
    public List<LocationOption> listPrefectureCities(String provinceId) {
        if (provinceId == null || provinceId.isBlank()) {
            return List.of();
        }
        if (MUNICIPALITY_IDS.contains(provinceId)) {
            Map<String, Object> row = locationMapper.selectById(provinceId);
            if (row == null) {
                return List.of();
            }
            return List.of(toOption(row));
        }
        List<Map<String, Object>> rows = locationMapper.selectCitiesByProvince(provinceId);
        if (!rows.isEmpty()) {
            return rows.stream().map(this::toOption).toList();
        }
        Map<String, Object> prov = locationMapper.selectById(provinceId);
        if (prov == null) {
            return List.of();
        }
        if (isLevelOne(prov)) {
            return List.of(toOption(prov));
        }
        return List.of();
    }

    @Override
    public RegionContext resolveContext(String locationId) {
        if (locationId == null || locationId.isBlank()) {
            return new RegionContext("", "", "", "");
        }
        Map<String, Object> cityRow = null;
        try {
            cityRow = locationMapper.selectById(locationId);
        } catch (Exception ignored) {
            // locations 表不存在等情况
        }
        if (cityRow == null) {
            String nm = doctorMapper.selectLocationNameByLocationId(locationId);
            if (nm != null && !nm.isBlank()) {
                return new RegionContext("", "", locationId, nm);
            }
            return new RegionContext("", "", "", "");
        }
        String cityName = stringVal(cityRow.get("name"));
        Object pidObj = cityRow.get("parentId");
        String parentId = pidObj == null ? "" : String.valueOf(pidObj).trim();
        if (parentId.isEmpty()) {
            return new RegionContext(locationId, cityName, locationId, cityName);
        }
        Map<String, Object> provRow = locationMapper.selectById(parentId);
        String pName = provRow == null ? "" : stringVal(provRow.get("name"));
        return new RegionContext(parentId, pName, locationId, cityName);
    }

    private LocationOption toOption(Map<String, Object> row) {
        LocationOption o = new LocationOption();
        o.setLocationId(stringVal(row.get("id")));
        o.setLocationName(stringVal(row.get("name")));
        return o;
    }

    private static String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private static boolean isLevelOne(Map<String, Object> row) {
        Object lv = row.get("levelType");
        if (lv instanceof Number n) {
            return n.intValue() == 1;
        }
        return "1".equals(String.valueOf(lv));
    }
}
