-- =============================================================================
-- 地区字典表 locations（表内列名仍为 localtion_* 历史拼写；编码与 doctor.location_id 一致，存地级市）
-- 执行顺序：先本文件建表，再执行 locations_data.sql 导入数据
-- 数据说明：locations_data.sql 由 pc-code.json（modood/Administrative-divisions-of-China）
--          脚本生成；含 31 个省级单位 + 下属地市/区县；并补充台湾省、港澳省级行。
--          直辖市（京/津/沪/渝）第二级为「区/县」而非地级市，与国家统计局常用划分一致。
-- =============================================================================

CREATE TABLE IF NOT EXISTS `locations` (
  `localtion_id` varchar(32) NOT NULL COMMENT '区划主键，与 doctor.location_id 一致（地级市或直辖市省码）',
  `localtion_name` varchar(64) NOT NULL COMMENT '展示名，可与 doctor.location_name 一致',
  `parent_localtion_id` varchar(32) DEFAULT NULL COMMENT '父级 localtion_id；省级为 NULL',
  `level_type` tinyint NOT NULL COMMENT '1=省/直辖市/自治区；2=地级市或直辖市区县',
  `sort_no` int NOT NULL DEFAULT 0 COMMENT '同级排序，越小越靠前',
  PRIMARY KEY (`localtion_id`),
  KEY `idx_parent` (`parent_localtion_id`),
  KEY `idx_level` (`level_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='省市区划字典';
