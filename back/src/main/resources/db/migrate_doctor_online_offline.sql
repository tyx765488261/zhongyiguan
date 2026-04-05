-- doctor 表：在线问诊 / 预约挂号 开关（与小程序 visitMode=online|offline 对应）
-- 执行一次即可；已有列可忽略报错或手动跳过

ALTER TABLE `doctor`
  ADD COLUMN `online` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否支持在线问诊' AFTER `phone`,
  ADD COLUMN `offline` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否支持预约挂号' AFTER `online`;
