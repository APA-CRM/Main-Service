INSERT INTO organization_file (
    id, file_id, organization_id,
    is_root, created_at, updated_at
) VALUES
(
    RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c1', 100,
    false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c2', 100,
    false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c3', 100,
    true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);