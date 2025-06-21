INSERT INTO organization(
    id, name, address, city,
    country, email
) VALUES
(
    100, 'NewPoshta', 'Criminal 52', 'Kyiv',
    'Ukraine', 'avraamkablyk@gmail.com'
);

INSERT INTO organization_user(
    id, organization_id, user_id
) VALUES (
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c9', 100, 1
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c8', 100, 2
),
(
    '5682d1e7-3eb4-4e41-923a-7b7abc0239c7', 100, 3
);

INSERT INTO organization_role(
    id, role_id, organization_id, role_type
) VALUES
('5682d1e7-3eb4-4e41-923a-7b7abc0239c9', 1, 100, 'ADMIN'),
('5682d1e7-3eb4-4e41-923a-7b7abc0239c8', 2, 100, 'MEMBER'),
('5682d1e7-3eb4-4e41-923a-7b7abc0239c7', 3, 100, 'USER'),
('5682d1e7-3eb4-4e41-923a-7b7abc0239c6', 4, 100, 'USER');

INSERT INTO organization_role_user(
    id, organization_role_id, organization_user_id
) VALUES
(RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c9', '5682d1e7-3eb4-4e41-923a-7b7abc0239c9'),
(RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c8', '5682d1e7-3eb4-4e41-923a-7b7abc0239c8'),
(RANDOM_UUID(), '5682d1e7-3eb4-4e41-923a-7b7abc0239c7', '5682d1e7-3eb4-4e41-923a-7b7abc0239c7');
