-- Appointment room-only migration.
-- Run the diagnostic queries first and handle any room_id IS NULL rows manually.

-- 1. Historical dirty data: appointment building_id does not match room building_id.
SELECT
    a.id AS appointment_id,
    a.building_id AS appointment_building_id,
    r.building_id AS room_building_id,
    a.room_id
FROM appointment a
INNER JOIN room r ON a.room_id = r.id
WHERE a.building_id <> r.building_id;

-- 2. Historical rows that cannot be migrated automatically.
SELECT
    a.id AS appointment_id,
    a.user_id,
    a.appointment_time,
    a.contact_name,
    a.contact_phone
FROM appointment a
WHERE a.room_id IS NULL;

-- 3. Optional compatibility repair before dropping building_id.
UPDATE appointment a
INNER JOIN room r ON a.room_id = r.id
SET a.building_id = r.building_id
WHERE a.room_id IS NOT NULL
  AND a.building_id <> r.building_id;

-- 4. After every row has a valid room_id, apply the schema change.
ALTER TABLE appointment
    DROP FOREIGN KEY fk_appointment_building;

ALTER TABLE appointment
    DROP INDEX idx_appointment_building_status;

ALTER TABLE appointment
    MODIFY room_id BIGINT NOT NULL COMMENT '房源ID';

ALTER TABLE appointment
    ADD KEY idx_appointment_room_status (room_id, status);

ALTER TABLE appointment
    DROP COLUMN building_id;
