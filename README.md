# spring-boot-member-api

### 요구사항
#### 사용자 요구사항
* 이메일, 닉네임, 이름, 전화번호, 비밀번호
#### 기능 요구사항
* 회원 가입
  * 전화번호 인증 후 회원 가입
* 로그인
* 내 정보 보기
* 비밀번호 찾기/재설정
  * 로그인 되어 있지 않은 상태로 가능
  * 전화번호 인증 후 비밀번호 재설정

### 구성
SpringBoot + Spring Data JPA + MariaDB Driver + Spring Security + JWT
* 빌드: Gradle
* 언어: Java
* 기타: yaml-resource-bundle, flywaydb, swagger3

#### API 문서
* [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
  * active profile이 local만 동작
##### 작업 설명
* 인증 문자 발송은 실 구현이 안되어 있어 가이드 API로 발송된 인증번호 조회 가능하도록 기능 제공
* 회원 가입시 이름은 AES 양방향 암호화되어 저장
* 비밀번호 찾기 / 재설정
  * 가입자 계정 검색 API로 계정 확인 (검색 유형: 이메일, 닉네임, 전화번호)
    * 검색 응답값으로 가입자 전화번호는 마스킹 처리
  * 전화번호 인증시 회원 가입시 기입한 전화번호로 인증을 받아야 비밀번호 재설정 가능
* 로그인 성공시 JWT 토큰 발행, 내정보 조회 API의 Header 로 토큰 전달
* 내 정보 조회시 이름은 복호화 처리
* Bean Validation 및 Custom Exception 오류 문구는 파일 관리 되도록 처리

### 시작하기
1. 프로젝트 다운
```shell
$ git clone https://github.com/devwue/spring-boot-member-api
```
2. 프로젝트 DB 스키마 준비
```shell
project-root]$ ./gradlew flywayMigrate && ./gradlew flywayInfo
```
3. Build & Run (커맨드 라인 실행시...)
```shell
project-root]$ ./gradlew clean build 
project-root]$ java -Dspring.profiles.active=local -server -jar build/libs/member-api-0.0.1-SNAPSHOT.jar 
```

#### Local 환경 요구 사항
1. docker client - 맥 기준
```shell
$ brew install docker
```

2. docker - DB 포트는 3307
```shell
project-root]$ docker composer -f docker-db.yml -d up
```