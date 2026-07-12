USE smart_sales_control;

ALTER TABLE room
    ADD COLUMN cover VARCHAR(255) DEFAULT NULL COMMENT '封面图' AFTER price,
    ADD COLUMN images JSON DEFAULT NULL COMMENT '房源图库JSON数组' AFTER cover;
