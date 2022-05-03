# spring-boot-member-api

### 요구사항
* 회원가입
* 로그인
* 핸드폰 인증
* 내 정보 보기
* 비밀번호 찾기/재설정

### 구성
SpringBoot + JPA

### 시작하기
1. 프로젝트 다운
```shell
$ git clone https://github.com/devwue/spring-boot-member-api
```
2. 프로젝트 DB 스키마 준비
```shell
project-root]$ ./gradlew flywayValidate --info
```
3. Build & Run (커맨드 라인 실행시...)
```shell
project-root]$ ./gradlew clean build 
project-root]$ java -server -jar build/libs/member-api-0.0.1-SNAPSHOT.jar 
```

### Local 환경 의존성
1. docker
```shell
project-root]$ docker composer -f docker-db.yml
```