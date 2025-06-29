INSERT INTO organization_invitation
(id, organization_id, user_id, role_id, invitor_id, status, expired_at)
VALUES
-- Valid
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d1', 100, 10, '5682d1e7-3eb4-4e41-923a-7b7abc0239c8', 1, 'PENDING', DATEADD(DAY, 3, CURRENT_TIMESTAMP)),
-- Expired
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d2', 100, 11, '5682d1e7-3eb4-4e41-923a-7b7abc0239c7', 1, 'PENDING', DATEADD(DAY, -3, CURRENT_TIMESTAMP)),
-- Declined
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d3', 100, 12, '5682d1e7-3eb4-4e41-923a-7b7abc0239c7', 2, 'DECLINED', DATEADD(DAY, 3, CURRENT_TIMESTAMP)),
-- Accepted
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d4', 100, 13, '5682d1e7-3eb4-4e41-923a-7b7abc0239c6', 3, 'ACCEPTED', DATEADD(DAY, 3, CURRENT_TIMESTAMP));