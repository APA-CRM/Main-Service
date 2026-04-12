INSERT INTO task_priority(id, name, color, organization_id,
                          created_at, updated_at)
VALUES (100, 'Low', '#00FF00', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (101, 'Medium', '#FFFF00', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (102, 'High', '#FF4500', 100,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);