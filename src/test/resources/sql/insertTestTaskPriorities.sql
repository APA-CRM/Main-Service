INSERT INTO task_priority(id, name, color, organization_id,
                          created_at, updated_at)
VALUES (100, 'Low', '#00FF00', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (101, 'Medium', '#FFFF00', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (102, 'High', '#FF4500', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (103, 'Low', '#00FF00', 101,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (104, 'Medium', '#FFFF00', 101,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (105, 'High', '#FF4500', 101,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);