use member_service;
insert into phone_authentication(feature, phone_number, phone_token, status, created_at, updated_at)
 VALUES ('SIGN_UP', '01012345678', '123456', 1, now(), now())
,('SIGN_UP', '01012345678', '223456', 2, now(), now());