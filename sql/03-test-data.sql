USE smart_sales_control;

-- Test accounts. Password for all accounts is: 123456
INSERT INTO user (
    id,
    username,
    password,
    real_name,
    phone,
    primary_role_code,
    status,
    create_user,
    update_user
) VALUES
    (2, 'customer01', '$2a$10$1OrCHMCJhNTcKNGCUso41ezVZLmOTai86FT0rCDdDua72wRPaO4gu', 'Customer One', '13900000001', 'CUSTOMER', 1, 1, 1),
    (3, 'sales01', '$2a$10$1OrCHMCJhNTcKNGCUso41ezVZLmOTai86FT0rCDdDua72wRPaO4gu', 'Sales One', '13900000002', 'SALES', 1, 1, 1)
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    real_name = VALUES(real_name),
    phone = VALUES(phone),
    primary_role_code = VALUES(primary_role_code),
    status = VALUES(status),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

INSERT INTO user_role (
    user_id,
    role_id,
    create_user,
    update_user
) VALUES
    (2, 3, 1, 1),
    (3, 2, 1, 1)
ON DUPLICATE KEY UPDATE
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

-- Test building, unit and room.
INSERT INTO building (
    id,
    name,
    cover,
    banner_images,
    developer,
    address,
    description,
    opening_time,
    delivery_time,
    status,
    deleted,
    create_user,
    update_user
) VALUES (
    1,
    'Test Building A',
    '',
    JSON_ARRAY(),
    'Test Developer',
    'Test Address',
    'API integration test building',
    '2026-08-01 10:00:00',
    '2027-12-01 10:00:00',
    1,
    0,
    1,
    1
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    cover = VALUES(cover),
    banner_images = VALUES(banner_images),
    developer = VALUES(developer),
    address = VALUES(address),
    description = VALUES(description),
    opening_time = VALUES(opening_time),
    delivery_time = VALUES(delivery_time),
    status = VALUES(status),
    deleted = VALUES(deleted),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

INSERT INTO building_unit (
    id,
    building_id,
    name,
    sort_no,
    status,
    deleted,
    create_user,
    update_user
) VALUES (
    1,
    1,
    'Unit 1',
    1,
    1,
    0,
    1,
    1
) ON DUPLICATE KEY UPDATE
    building_id = VALUES(building_id),
    name = VALUES(name),
    sort_no = VALUES(sort_no),
    status = VALUES(status),
    deleted = VALUES(deleted),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

INSERT INTO room (
    id,
    building_id,
    unit_id,
    room_no,
    floor_no,
    area,
    price,
    layout,
    orientation,
    decoration,
    status,
    remark,
    deleted,
    create_user,
    update_user
) VALUES (
    1,
    1,
    1,
    '101',
    1,
    89.50,
    1200000.00,
    '3 bedrooms',
    'South',
    'Fine decoration',
    0,
    'Test room',
    0,
    1,
    1
) ON DUPLICATE KEY UPDATE
    building_id = VALUES(building_id),
    unit_id = VALUES(unit_id),
    room_no = VALUES(room_no),
    floor_no = VALUES(floor_no),
    area = VALUES(area),
    price = VALUES(price),
    layout = VALUES(layout),
    orientation = VALUES(orientation),
    decoration = VALUES(decoration),
    status = VALUES(status),
    remark = VALUES(remark),
    deleted = VALUES(deleted),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

-- Test notices.
INSERT INTO notice (
    id,
    title,
    content,
    publisher_id,
    target_role,
    status,
    publish_time,
    create_user,
    update_user
) VALUES
    (1, 'Test Notice', 'This is an Apifox integration test notice.', 1, 'ALL', 1, '2026-08-01 09:00:00', 1, 1),
    (2, 'Sales Notice', 'This notice is only visible to sales users.', 1, 'SALES', 1, '2026-08-01 09:30:00', 1, 1),
    (3, 'Customer Notice', 'This notice is only visible to customer users.', 1, 'CUSTOMER', 1, '2026-08-01 10:00:00', 1, 1)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    content = VALUES(content),
    publisher_id = VALUES(publisher_id),
    target_role = VALUES(target_role),
    status = VALUES(status),
    publish_time = VALUES(publish_time),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

-- Test appointments.
INSERT INTO appointment (
    id,
    user_id,
    sales_user_id,
    building_id,
    room_id,
    appointment_time,
    contact_name,
    contact_phone,
    remark,
    status,
    cancel_reason,
    create_user,
    update_user
) VALUES
    (1, 2, 3, 1, 1, '2026-08-10 10:00:00', 'Customer One', '13900000001', 'Assigned appointment', 2, NULL, 2, 1),
    (2, 2, NULL, 1, 1, '2026-08-11 14:00:00', 'Customer One', '13900000001', 'Pending assignment', 1, NULL, 2, 2),
    (3, 2, 3, 1, NULL, '2026-08-12 16:00:00', 'Customer One', '13900000001', 'Visited appointment', 3, NULL, 2, 1)
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id),
    sales_user_id = VALUES(sales_user_id),
    building_id = VALUES(building_id),
    room_id = VALUES(room_id),
    appointment_time = VALUES(appointment_time),
    contact_name = VALUES(contact_name),
    contact_phone = VALUES(contact_phone),
    remark = VALUES(remark),
    status = VALUES(status),
    cancel_reason = VALUES(cancel_reason),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;

-- Test customer follow records.
INSERT INTO customer_follow (
    id,
    appointment_id,
    customer_user_id,
    sales_user_id,
    follow_type,
    follow_content,
    next_follow_time,
    create_user,
    update_user
) VALUES (
    1,
    1,
    2,
    3,
    1,
    'Phone call completed. Customer has strong intent.',
    '2026-08-12 10:00:00',
    3,
    3
) ON DUPLICATE KEY UPDATE
    appointment_id = VALUES(appointment_id),
    customer_user_id = VALUES(customer_user_id),
    sales_user_id = VALUES(sales_user_id),
    follow_type = VALUES(follow_type),
    follow_content = VALUES(follow_content),
    next_follow_time = VALUES(next_follow_time),
    update_user = VALUES(update_user),
    update_time = CURRENT_TIMESTAMP;
