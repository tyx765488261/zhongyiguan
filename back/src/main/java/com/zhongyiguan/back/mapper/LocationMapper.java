package com.zhongyiguan.back.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LocationMapper {

    @Select("""
            SELECT localtion_id AS id, localtion_name AS name, parent_localtion_id AS parentId, level_type AS levelType
            FROM locations
            WHERE level_type = 1
            ORDER BY sort_no
            """)
    List<Map<String, Object>> selectProvinces();

    @Select("""
            SELECT localtion_id AS id, localtion_name AS name
            FROM locations
            WHERE parent_localtion_id = #{parentId}
            ORDER BY sort_no
            """)
    List<Map<String, Object>> selectCitiesByProvince(@Param("parentId") String parentId);

    @Select("""
            SELECT localtion_id AS id, localtion_name AS name, parent_localtion_id AS parentId, level_type AS levelType
            FROM locations
            WHERE localtion_id = #{id}
            """)
    Map<String, Object> selectById(@Param("id") String id);
}
