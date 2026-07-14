-- Add locked room status for sale control.
-- Room status after this migration: 0 available, 1 sold, 2 locked.
-- Existing rows remain unchanged; admins can set status = 2 after deployment.

ALTER TABLE room
    MODIFY status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待售，1已售，2锁定';
