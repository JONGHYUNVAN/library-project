# library-project
 ### library project by JHVAN   
#### 프로젝트 목적 :  사이트의 분위기와 여러 상호작용 요소로 사용자들의 사이트와 책에 대한 관심을 얻고, 로그인한 사용자들의 활동과 관심사에 관한 활동 데이터를 얻는 웹 사이트의 구현   

## 기술 스택 Tech Stack
**1. SpringBoot:**
**Spring Boot**
- ![SpringBoot](https://img.shields.io/badge/SpringBoot-2.7.0-6DB33F?style=for-the-badge&logo=SpringBoot)
    - 스프링 기반 애플리케이션 개발을 위한 오픈소스 프레임워크
    - 의존성 주입, 자동 설정 등의 기능 제공

**Spring Data JPA**
- ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-2.7.0-6DB33F?style=for-the-badge&logo=Spring)
    - JPA 기반의 데이터 접근 계층을 쉽게 구현할 수 있도록 지원하는 프레임워크

**Spring Security**
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-2.7.0-6DB33F?style=for-the-badge&logo=Spring)
    - 인증(Authentication)과 인가(Authorization) 기능 제공
    - JWT, OAuth2 등을 기반으로 사용자 인증 구현

**JSON Web Token**
- ![JWT](https://img.shields.io/badge/JWT-0.11.5-000000?style=for-the-badge&logo=JSON%20web%20tokens)
    - 토큰 기반 인증을 위한 오픈 스탠다드
    - 토큰에 사용자 정보를 안전하게 저장/전송

**MySQL**
- ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)
    - 오픈소스 관계형 데이터베이스 관리 시스템
    - 대용량 웹 애플리케이션을 위한 데이터 저장소

**Lombok**
- ![Lombok](https://img.shields.io/badge/Lombok-1.18.22-4882CF?style=for-the-badge&logo=Lombok&logoColor=white)
    - 코드 자동 생성 라이브러리
    - DTO 모델 클래스 작성 시 getter/setter등 자동 생성

**H2 Database**
- ![H2 Database](https://img.shields.io/badge/H2%20Database-1.4.200-2925E9?style=for-the-badge&logo=H2&logoColor=white)
    - 인메모리 관계형 데이터베이스
    - 테스트 용도로 주로 사용

**JUnit**
- ![JUnit](https://img.shields.io/badge/JUnit-5.8.2-25A162?style=for-the-badge&logo=JUnit5&logoColor=white)
    - 자바 프로그램을 위한 유닛 테스트 프레임워크
    - 테스트 코드 작성에 사용


## 구현 상세 :    

+ 요구사항 정의 :
![img_13.png](img_13.png)


+ DB Diagram :   
+ https://dbdocs.io/billlys2/Library-Project-by-J.H.Van-DBDiagram
![DBdiagram.png](DBdiagram.png)
 

+ 코드 스타일:  
Controller: 
![img_15.png](img_15.png)
가독성을 위해 메인 도메인 관련 mapper 나 service 레이어명 앞엔 도메인 이름을 붙이지 않음.  
return 문의 가독성 향항을 위해 ResponseCreator 클래스를 만들어 타입 가독성을 높임.  
관심사 분리와 재사용 가능성을 고려하여 DTO->Entity 변환 클래스와 엔티티 업데이트 클래스를 분리함.  

DTO:  
![img_16.png](img_16.png)  
custom validation 을 사용하여 각 상황에 적합한 벨리데이션 구현.  
또한 각 상황별 필요한 DTO 를 따로 만들어 데이터 통신 효율을 높임.

Entity:  
![img_17.png](img_17.png)  
DB에 전달하기 전 한번 더 벨리데이션 하여 더블체그 하도록 함.  

Mapper:  
![img_18.png](img_18.png)  
제네릭 타입을 사용하여 필드명이 일치하는 필드끼리 매핑하는 mapper 클래스를 사용하여 DTO 변경만으로 서비스 변경사항에 대응할 수 있음.

Repository:  
![img_20.png](img_20.png)  
확장형 인터페이스를 활용하여 언젠가 일어날 수 있는 migration 에 대응할 때 모든코드 변경 없이 확장자명만 변경하여 대응할 수 있음.

Service:
![img_21.png](img_21.png)  
메소드 분리를 통해 코드의 재사용을 용이하게 하여 유지보수성을 높이고, 짧아진 코드의 가독성도 높임.
대부분의 메소드를 public 메소드로 사용하여 필요하다면 다른 서비스에서도 자유롭게 필요한 메소드만 사용할 수 있음.
