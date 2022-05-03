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