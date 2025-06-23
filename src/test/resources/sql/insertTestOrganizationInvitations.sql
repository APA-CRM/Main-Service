INSERT INTO organization_invitation
(id, organization_id, user_id, invitor_id, status, expired_at)
VALUES
-- Valid
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d1', 100, 10, 1, 'PENDING', DATEADD(DAY, 3, CURRENT_TIMESTAMP)),
-- Expired
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d2', 100, 11, 1, 'PENDING', DATEADD(DAY, -3, CURRENT_TIMESTAMP)),
-- Declined
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d3', 100, 12, 2, 'DECLINED', DATEADD(DAY, 3, CURRENT_TIMESTAMP)),
-- Accepted
('6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d4', 100, 13, 3, 'ACCEPTED', DATEADD(DAY, 3, CURRENT_TIMESTAMP));