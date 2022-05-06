CREATE DATABASE IF NOT EXISTS member_service default CHARACTER SET UTF8MB4;
use member_service;
--
Drop table if exists member;
CREATE TABLE if NOT EXISTS member (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(50) not null COMMENT '이메일',
  name VARCHAR(60) not NULL COMMENT '이름',
  nick_name VARCHAR(10) not NULL COMMENT '닉네임',
  phone_agency VARCHAR(4) not null COMMENT '통신사',
  phone_number VARCHAR(11) not null COMMENT '핸드폰 번호',
  phone_validate boolean not null default false comment '핸드폰 인증',
  password varchar(100) not null comment '비밀번호 hash',
  sign_in_at datetime null comment '최종 로그인 일자',
  created_at datetime not null comment '생성 일자',
  updated_at datetime not null comment '수정 일자',
  unique key idx_email(email),
  unique key idx_nick(nick_name),
  unique key idx_phone_number(phone_number)
) engine innodb comment = '회원 정보';

Drop table if exists phone_authentication;
CREATE TABLE if not exists phone_authentication (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 feature VARCHAR(50) not null COMMENT 'signup, password',
 phone_number VARCHAR(11) not null COMMENT '핸드폰 번호',
 phone_token varchar(6) not null comment '인증 문자',
 status tinyint default 0 comment '0: 미발송, 1: 발송 성공, 2: 인증 완료, -1: 발송 실패, -2:만료',
 created_at datetime not null comment '생성 일자',
 updated_at datetime not null comment '수정 일자',
 key idx_email_phoneNumber(feature, phone_number)
) engine innodb comment = '핸드폰 인증 번호';