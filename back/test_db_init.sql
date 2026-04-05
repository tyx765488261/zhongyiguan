USE test;

-- 1. 健康资讯表 (用于首页文章)
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `title` VARCHAR(255) NOT NULL COMMENT '文章标题',
    `tag` VARCHAR(50) COMMENT '标签(如:节气养生,常见病)',
    `date` VARCHAR(20) COMMENT '发布日期(如:03-28)',
    `content` TEXT COMMENT '文章内容(预留)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康资讯表';

-- 2. 科室表 (用于科室选择页)
CREATE TABLE IF NOT EXISTS `department` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '科室名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级科室ID(0为一级科室)',
    `order_num` INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 3. 常见疾病表 (用于疾病选择页)
CREATE TABLE IF NOT EXISTS `disease` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '疾病名称',
    `category` VARCHAR(50) COMMENT '疾病分类(如:呼吸系统,消化系统)',
    `order_num` INT DEFAULT 0 COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常见疾病表';

-- 4. 医生信息表 (用于我的医生)
CREATE TABLE IF NOT EXISTS `doctor` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
    `title` VARCHAR(50) COMMENT '职称(如:主任医师)',
    `dept_name` VARCHAR(100) COMMENT '所属科室',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `specialty` TEXT COMMENT '擅长领域'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- 5. 用户订单表 (用于我的订单)
CREATE TABLE IF NOT EXISTS `user_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '关联用户ID',
    `order_no` VARCHAR(100) NOT NULL COMMENT '订单号',
    `service_name` VARCHAR(100) COMMENT '服务名称(如:在线问诊)',
    `amount` DECIMAL(10,2) COMMENT '金额',
    `status` INT COMMENT '状态(0:待支付, 1:已支付, 2:已取消)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户订单表';

-- 6. 就诊记录表 (用于我的就诊记录)
CREATE TABLE IF NOT EXISTS `user_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '关联用户ID',
    `doctor_name` VARCHAR(50) COMMENT '接诊医生',
    `dept_name` VARCHAR(100) COMMENT '就诊科室',
    `visit_date` DATE COMMENT '就诊日期',
    `diagnosis` TEXT COMMENT '初步诊断'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就诊记录表';

-- 7. 检查报告表 (用于我的报告)
CREATE TABLE IF NOT EXISTS `user_report` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(50) NOT NULL COMMENT '关联用户ID',
    `report_name` VARCHAR(100) NOT NULL COMMENT '报告名称(如:血常规)',
    `check_date` DATE COMMENT '检查日期',
    `result` TEXT COMMENT '检查结果(简述)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查报告表';

-- 插入测试数据
INSERT INTO `article` (title, tag, date) VALUES 
('春季养肝：饮食与作息要点', '节气养生', '03-28'),
('失眠与心神：中医如何辨证', '常见病', '03-25'),
('艾灸入门：注意事项一览', '外治法', '03-20');

INSERT INTO `department` (name, parent_id, order_num) VALUES 
('内科', 0, 1), ('外科', 0, 2), ('妇科', 0, 3), ('儿科', 0, 4),
('呼吸内科', 1, 1), ('消化内科', 1, 2), ('神经内科', 1, 3);

INSERT INTO `disease` (name, category, order_num) VALUES 
('发热', '常见症状', 1), ('咳嗽', '常见症状', 2), ('感冒', '呼吸系统', 1), ('胃炎', '消化系统', 1);