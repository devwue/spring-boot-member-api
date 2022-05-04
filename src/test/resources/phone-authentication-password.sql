use member_service;
insert into phone_authentication(feature, phone_number, phone_token, status, created_at, updated_at)
 VALUES ('RESET_PASSWORD', '01012345678', '223456', 2, now(), now())
, ('RESET_PASSWORD', '01012345678', '223456', 1, now(), now());
