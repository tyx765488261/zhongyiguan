package com.zhongyiguan.back.service;

import com.zhongyiguan.back.entity.LocationOption;
import com.zhongyiguan.back.entity.LocationPickerResponse;
import com.zhongyiguan.back.entity.LocationProvinceGroup;
import com.zhongyiguan.back.entity.RegionContext;

import java.util.List;

public interface ILocationService {

    /**
     * 地点下拉完整数据：各省 + 下属地级市（一次请求），仅 locations 字典，与是否有医生无关。
     */
    List<LocationProvinceGroup> listPickerTree();

    /**
     * 地区筛选专用：一次返回 {@link #listPickerTree()} 与当前 locationId 的回显信息。
     */
    LocationPickerResponse buildPickerResponse(String locationId);

    /** 省级列表（不含「全国」，由前端自行拼） */
    List<LocationOption> listProvinces();

    /**
     * 某省下的地级市选项（doctor.location_id 仅保存这一层）。
     * 直辖市返回唯一一项（省本级编码）；无下级数据的省返回省本级一项可选。
     */
    List<LocationOption> listPrefectureCities(String provinceId);

    /** 根据已选地级市 id 回显省、市 */
    RegionContext resolveContext(String locationId);
}
