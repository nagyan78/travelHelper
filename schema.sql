-- ========================================
-- 旅游攻略系统 - 数据库建表脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `travel_itinerary` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `travel_itinerary`;

-- ========================================
-- 1. 用户模块
-- ========================================

-- 用户表
CREATE TABLE `t_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `gender` TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `signature` VARCHAR(200) DEFAULT NULL COMMENT '个性签名',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 2. 攻略模块
-- ========================================

-- 攻略主表
CREATE TABLE `t_itinerary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '攻略标题',
  `destination` VARCHAR(100) NOT NULL COMMENT '目的地',
  `days` INT NOT NULL COMMENT '旅行天数',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `budget` DECIMAL(10,2) DEFAULT NULL COMMENT '总预算',
  `ticket_budget` DECIMAL(10,2) DEFAULT NULL COMMENT '门票预算',
  `transport_budget` DECIMAL(10,2) DEFAULT NULL COMMENT '交通预算',
  `food_budget` DECIMAL(10,2) DEFAULT NULL COMMENT '美食预算',
  `style` VARCHAR(50) DEFAULT NULL COMMENT '旅行风格',
  `html_content` LONGTEXT COMMENT 'HTML格式内容',
  `json_data` LONGTEXT COMMENT 'JSON格式数据(用于编辑)',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `share_count` INT NOT NULL DEFAULT 0 COMMENT '分享次数',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿, 1-已发布, 2-已删除',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_destination` (`destination`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='攻略主表';

-- 每日行程表
CREATE TABLE `t_itinerary_day` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `itinerary_id` BIGINT NOT NULL COMMENT '攻略ID',
  `day_number` INT NOT NULL COMMENT '第几天(从1开始)',
  `date` DATE NOT NULL COMMENT '具体日期',
  `departure_time` TIME DEFAULT NULL COMMENT '出发时间',
  `theme` VARCHAR(100) DEFAULT NULL COMMENT '当日主题',
  `weather_condition` VARCHAR(50) DEFAULT NULL COMMENT '天气状况',
  `weather_temp_min` DECIMAL(5,2) DEFAULT NULL COMMENT '最低温度',
  `weather_temp_max` DECIMAL(5,2) DEFAULT NULL COMMENT '最高温度',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_itinerary_id` (`itinerary_id`),
  KEY `idx_date` (`date`),
  UNIQUE KEY `uk_itinerary_day` (`itinerary_id`, `day_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日行程表';

-- 景点时间表
CREATE TABLE `t_itinerary_spot` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `day_id` BIGINT NOT NULL COMMENT '日程ID',
  `time` VARCHAR(10) NOT NULL COMMENT '时间点(如 09:00)',
  `activity` VARCHAR(500) NOT NULL COMMENT '活动描述',
  `spot_name` VARCHAR(100) DEFAULT NULL COMMENT '景点名称',
  `spot_address` VARCHAR(255) DEFAULT NULL COMMENT '景点地址',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '经度',
  `ticket_price` DECIMAL(10,2) DEFAULT NULL COMMENT '门票价格',
  `need_booking` TINYINT DEFAULT 0 COMMENT '是否需要预约: 0-否, 1-是',
  `booking_advance_days` INT DEFAULT NULL COMMENT '需提前预约天数',
  `duration` INT DEFAULT NULL COMMENT '建议游玩时长(分钟)',
  `transport_from_prev` VARCHAR(50) DEFAULT NULL COMMENT '从上一点交通方式',
  `transport_cost` DECIMAL(10,2) DEFAULT NULL COMMENT '交通费用',
  `transport_duration` INT DEFAULT NULL COMMENT '交通时长(分钟)',
  `tips` VARCHAR(500) DEFAULT NULL COMMENT '小贴士',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_day_id` (`day_id`),
  KEY `idx_spot_name` (`spot_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点时间表';

-- 攻略标签表
CREATE TABLE `t_itinerary_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `itinerary_id` BIGINT NOT NULL COMMENT '攻略ID',
  `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_itinerary_id` (`itinerary_id`),
  KEY `idx_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='攻略标签表';

-- 攻略收藏表
CREATE TABLE `t_itinerary_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `itinerary_id` BIGINT NOT NULL COMMENT '攻略ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_itinerary` (`user_id`, `itinerary_id`),
  KEY `idx_itinerary_id` (`itinerary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='攻略收藏表';

-- ========================================
-- 3. 系统模块
-- ========================================

-- 操作日志表
CREATE TABLE `t_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '模块名称',
  `action` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '操作描述',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
  `request_params` TEXT COMMENT '请求参数(JSON)',
  `response_result` TEXT COMMENT '响应结果',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '浏览器信息',
  `execution_time` INT DEFAULT NULL COMMENT '执行时长(ms)',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
  `error_msg` TEXT COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 系统配置表
CREATE TABLE `t_system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT COMMENT '配置值',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ========================================
-- 4. 初始化数据
-- ========================================

-- 插入系统配置
INSERT INTO `t_system_config` (`config_key`, `config_value`, `description`) VALUES
('ai.model.name', 'qwen-max', 'AI模型名称'),
('ai.model.temperature', '0.7', 'AI生成温度'),
('ai.model.max_tokens', '4000', 'AI最大Token数'),
('weather.cache.hours', '1', '天气缓存时长(小时)'),
('itinerary.draft.auto_save.interval', '30000', '草稿自动保存间隔(ms)'),
('user.token.expiration', '1800', '用户Token过期时间(秒)');

-- 插入测试用户 (密码: 123456, BCrypt加密后)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `email`, `phone`) VALUES
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'test@example.com', '13800138000');

-- ========================================
-- 5. 索引优化说明
-- ========================================
-- 主要索引已在建表时创建
-- 后续可根据查询需求添加复合索引

-- ========================================
-- 完成
-- ========================================


