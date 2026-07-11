CREATE DATABASE IF NOT EXISTS smart_sales_control
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE smart_sales_control;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(64) NOT NULL COMMENT '登录账号',
    password VARCHAR(128) NOT NULL COMMENT '加密密码',
    real_name VARCHAR(64) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    wx_openid VARCHAR(64) DEFAULT NULL COMMENT '微信OpenID',
    avatar_url VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    primary_role_code VARCHAR(32) NOT NULL COMMENT '主角色编码，兼容单角色快速判断',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_phone (phone),
    UNIQUE KEY uk_user_wx_openid (wx_openid),
    KEY idx_user_primary_role_status (primary_role_code, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_code VARCHAR(32) NOT NULL COMMENT '角色编码：ADMIN、SALES、CUSTOMER',
    role_name VARCHAR(64) NOT NULL COMMENT '角色名称',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序号',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code),
    KEY idx_role_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS user_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户角色关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_role_role_id (role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS building (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '楼盘ID',
    name VARCHAR(128) NOT NULL COMMENT '楼盘名称',
    city VARCHAR(64) NOT NULL COMMENT '城市',
    district VARCHAR(64) DEFAULT NULL COMMENT '区域',
    address VARCHAR(255) NOT NULL COMMENT '详细地址',
    developer VARCHAR(128) DEFAULT NULL COMMENT '开发商',
    sale_office_phone VARCHAR(32) DEFAULT NULL COMMENT '售楼处电话',
    cover_image VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    description VARCHAR(1000) DEFAULT NULL COMMENT '楼盘简介',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1在售，2待售，3售罄，0下架',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    KEY idx_building_city_status (city, status),
    KEY idx_building_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼盘表';

CREATE TABLE IF NOT EXISTS building_image (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '楼盘图片ID',
    building_id BIGINT NOT NULL COMMENT '楼盘ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片地址',
    image_type TINYINT NOT NULL DEFAULT 1 COMMENT '图片类型：1效果图，2实景图，3户型图，4配套图',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    KEY idx_building_image_building (building_id, image_type, status),
    CONSTRAINT fk_building_image_building FOREIGN KEY (building_id) REFERENCES building (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼盘图片表';

CREATE TABLE IF NOT EXISTS unit (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '楼栋/单元ID',
    building_id BIGINT NOT NULL COMMENT '楼盘ID',
    parent_id BIGINT DEFAULT NULL COMMENT '父级ID，楼栋为空，单元指向楼栋',
    unit_type TINYINT NOT NULL COMMENT '节点类型：1楼栋，2单元',
    name VARCHAR(64) NOT NULL COMMENT '名称',
    unit_no VARCHAR(32) NOT NULL COMMENT '编号',
    floor_count INT NOT NULL COMMENT '楼层数',
    room_count_per_floor INT DEFAULT NULL COMMENT '每层房源数，单元节点使用',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0停用',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_unit_building_parent_no (building_id, parent_id, unit_no),
    KEY idx_unit_building_type (building_id, unit_type, status),
    KEY idx_unit_parent_id (parent_id),
    CONSTRAINT fk_unit_building FOREIGN KEY (building_id) REFERENCES building (id),
    CONSTRAINT fk_unit_parent FOREIGN KEY (parent_id) REFERENCES unit (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋单元表';

CREATE TABLE IF NOT EXISTS room (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '房源ID',
    building_id BIGINT NOT NULL COMMENT '楼盘ID',
    building_unit_id BIGINT NOT NULL COMMENT '楼栋ID，对应unit表楼栋节点',
    unit_id BIGINT NOT NULL COMMENT '单元ID，对应unit表单元节点',
    room_no VARCHAR(32) NOT NULL COMMENT '房号',
    floor_no INT NOT NULL COMMENT '楼层',
    house_type VARCHAR(64) DEFAULT NULL COMMENT '户型',
    orientation VARCHAR(32) DEFAULT NULL COMMENT '朝向',
    construction_area DECIMAL(10,2) NOT NULL COMMENT '建筑面积',
    usable_area DECIMAL(10,2) DEFAULT NULL COMMENT '套内面积',
    total_price DECIMAL(14,2) NOT NULL COMMENT '总价',
    unit_price DECIMAL(12,2) NOT NULL COMMENT '单价',
    status VARCHAR(32) NOT NULL DEFAULT 'AVAILABLE' COMMENT '房源状态：AVAILABLE待售，RESERVED已预订，SOLD已售，UNAVAILABLE不可售',
    description VARCHAR(1000) DEFAULT NULL COMMENT '房源说明',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_room_unit_room_no (unit_id, room_no),
    KEY idx_room_building_id (building_id),
    KEY idx_room_building_floor (building_unit_id, floor_no),
    KEY idx_room_status (status),
    KEY idx_room_no (room_no),
    CONSTRAINT fk_room_building FOREIGN KEY (building_id) REFERENCES building (id),
    CONSTRAINT fk_room_building_unit FOREIGN KEY (building_unit_id) REFERENCES unit (id),
    CONSTRAINT fk_room_unit FOREIGN KEY (unit_id) REFERENCES unit (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房源表';

CREATE TABLE IF NOT EXISTS favorite (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    room_id BIGINT NOT NULL COMMENT '房源ID',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_favorite_user_room (user_id, room_id),
    KEY idx_favorite_room_id (room_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_favorite_room FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房源收藏表';

CREATE TABLE IF NOT EXISTS appointment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    user_id BIGINT NOT NULL COMMENT '购房用户ID',
    sales_user_id BIGINT DEFAULT NULL COMMENT '跟进销售ID',
    building_id BIGINT NOT NULL COMMENT '楼盘ID',
    room_id BIGINT DEFAULT NULL COMMENT '意向房源ID',
    appointment_time DATETIME NOT NULL COMMENT '预约看房时间',
    contact_name VARCHAR(64) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系人手机号',
    remark VARCHAR(500) DEFAULT NULL COMMENT '预约备注',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '预约状态：1待确认，2已确认，3已看房，4已取消，5已过期',
    cancel_reason VARCHAR(255) DEFAULT NULL COMMENT '取消原因',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    KEY idx_appointment_user_id (user_id),
    KEY idx_appointment_sales_user_id (sales_user_id),
    KEY idx_appointment_building_status (building_id, status),
    KEY idx_appointment_time (appointment_time),
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_appointment_sales_user FOREIGN KEY (sales_user_id) REFERENCES user (id),
    CONSTRAINT fk_appointment_building FOREIGN KEY (building_id) REFERENCES building (id),
    CONSTRAINT fk_appointment_room FOREIGN KEY (room_id) REFERENCES room (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='看房预约表';

CREATE TABLE IF NOT EXISTS notice (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(128) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    publisher_id BIGINT NOT NULL COMMENT '发布人ID',
    target_role VARCHAR(32) NOT NULL DEFAULT 'ALL' COMMENT '可见角色：ALL、ADMIN、SALES、CUSTOMER',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1发布，0下架',
    publish_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    KEY idx_notice_target_status (target_role, status),
    KEY idx_notice_publish_time (publish_time),
    CONSTRAINT fk_notice_publisher FOREIGN KEY (publisher_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '操作日志ID',
    operator_id BIGINT DEFAULT NULL COMMENT '操作人ID',
    operator_name VARCHAR(64) DEFAULT NULL COMMENT '操作人姓名',
    operator_role VARCHAR(32) DEFAULT NULL COMMENT '操作人角色',
    module_name VARCHAR(64) NOT NULL COMMENT '模块名称',
    operation_type VARCHAR(64) NOT NULL COMMENT '操作类型',
    operation_desc VARCHAR(255) DEFAULT NULL COMMENT '操作描述',
    request_uri VARCHAR(255) DEFAULT NULL COMMENT '请求地址',
    request_method VARCHAR(16) DEFAULT NULL COMMENT '请求方法',
    request_ip VARCHAR(64) DEFAULT NULL COMMENT '请求IP',
    target_id BIGINT DEFAULT NULL COMMENT '业务对象ID',
    success TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功：1成功，0失败',
    error_message VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    ext_json JSON DEFAULT NULL COMMENT '扩展字段',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user BIGINT DEFAULT NULL COMMENT '创建人ID',
    update_user BIGINT DEFAULT NULL COMMENT '更新人ID',
    PRIMARY KEY (id),
    KEY idx_operation_log_operator (operator_id),
    KEY idx_operation_log_module_time (module_name, create_time),
    KEY idx_operation_log_target (target_id),
    CONSTRAINT fk_operation_log_operator FOREIGN KEY (operator_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

INSERT INTO role (
    id,
    role_code,
    role_name,
    status,
    sort_no
) VALUES
    (1, 'ADMIN', '超级管理员', 1, 1),
    (2, 'SALES', '销售', 1, 2),
    (3, 'CUSTOMER', '购房用户', 1, 3)
ON DUPLICATE KEY UPDATE
    role_name = VALUES(role_name),
    status = VALUES(status),
    sort_no = VALUES(sort_no),
    update_time = CURRENT_TIMESTAMP;

INSERT INTO user (
    id,
    username,
    password,
    real_name,
    phone,
    primary_role_code,
    status
) VALUES (
    1,
    'admin',
    '$2a$10$1OrCHMCJhNTcKNGCUso41ezVZLmOTai86FT0rCDdDua72wRPaO4gu',
    '超级管理员',
    '13800000000',
    'ADMIN',
    1
) ON DUPLICATE KEY UPDATE
    real_name = VALUES(real_name),
    primary_role_code = VALUES(primary_role_code),
    status = VALUES(status),
    update_time = CURRENT_TIMESTAMP;

INSERT INTO user_role (
    user_id,
    role_id,
    create_user,
    update_user
) VALUES (
    1,
    1,
    1,
    1
) ON DUPLICATE KEY UPDATE
    update_time = CURRENT_TIMESTAMP,
    update_user = VALUES(update_user);
