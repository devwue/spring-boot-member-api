use member_service;
insert into member(id, email, name, nick_name, phone_agency, phone_number, phone_validate, password, sign_in_at, created_at, updated_at)
values (1, 'kildong.hong@devwue.com', '홍길동', '의적', 'SKT', '01012345678', false, '{bcrypt}$2a$10$e8UEUKN4M2JMxPyROvdxMOEgBCoYoOA0rguUPOhAdCFp1KEsRfMfy', null, now(), now())
on duplicate key update updated_at = now();