-- Simplify room and appointment statuses.
-- Run this once when upgrading an old database that still uses the previous
-- room status values: 0 available, 1 reserved, 2 sold, 3 unavailable.
-- Do not rerun after the new two-status room model is already in use.
-- Room status: 0 available, 1 sold.
-- Appointment status: 1 booked, 2 canceled, 3 expired.

UPDATE room
SET status = CASE
    WHEN status = 2 THEN 1
    ELSE 0
END
WHERE status NOT IN (0, 1)
   OR status = 1;

UPDATE appointment
SET status = CASE
    WHEN status = 4 THEN 2
    WHEN status = 5 THEN 3
    ELSE 1
END
WHERE status NOT IN (1, 2, 3)
   OR status IN (2, 3);

ALTER TABLE room
    MODIFY status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待售，1已售';

ALTER TABLE appointment
    MODIFY status TINYINT NOT NULL DEFAULT 1 COMMENT '预约状态：1已预约，2已取消，3已过期';
