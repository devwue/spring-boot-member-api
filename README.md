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
SpringBoot + JPA

#### API 문서
* [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
  * active profile이 local만 동작
* 인증 문자 발송은 실 구현이 안되어 있어 가이드 API로 인증 번호 확인
* 로그인 성공시 JWT 토큰 발행, 마이 페이지 접근시 인증 헤더에 포함해 전달 되어야 함.

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
project-root]$ java -server -jar build/libs/member-api-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=local 
```

### Local 환경 의존성
1. docker client - 맥 기준
```shell
$ brew install docker
```

2. docker - DB 포트는 3307
```shell
project-root]$ docker composer -f docker-db.yml -d up
```