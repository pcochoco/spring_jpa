### jpa 
Member별로 Item의 stock에 따른 Order 
- 각각 entity, repository, service를 통해 작동
- MVC pattern, thymeleaf로 웹 계층 

## 연관관계 mapping
다대일 관계에서 연관관계 주인 
= 외래키 보유한 쪽 
= 다 

- ManyToOne : @ManyToOne(fetch = FetchType.LAZY) @JoinColumn
- OneToMany : @OneToMany(mappedBy = "")
- @Enumerated(EnumType.STRING) : enum(여러가지 상수를 지닌 자료형)
- @ManyToMany x : 중간 엔티티(테이블)을 활용해 ManyToOne, OneToMany로 풀어냄
- @Embedded : @Embeddable -> 공통된 필드를 묶은 자료형
- id : entity에서는 type이 있기 때문에 구별이 가능하지만 table에서는 아니기 때문에 table명 + id 활용
- 연관관계 메서드 : 양방향 관계의 값을 맞춰줌




## 상속관계
- @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
- @DiscriminatorColumn(name = "dtype") - @DiscriminatorValue("example")

## entity 설계 시 주의점 
- Getter o, Setter x : 변경되지 않도록
- XToOne Mapping(ManyToOne, OneToOne) : EAGER(기본) -> LAZY
- collection은 field에서 초기화(hibernate은 감싸면서 내장 컬렉션으로 만들어버림)
- entity -> table : camel, . -> _ 대문자 -> 소문자
- 논리명 : 명시적으로 이름을 만들지 않으면 ImplicitNamingStrategy
- 물리명 : 모든 논리명에 적용, 실제 테이블에 적용


## Repository
- @Repository : spring bean 등록 
- @PersistenceContext(-> @RequiredArgsConstructor) : entity manager 주입 
## Service 
- @Transactional : PersistenceContext
- @Autowired(-> @RequiredArgsConstructor) : repository 주입
 
## Test
- @RunWith(SpringRunner.class) : 스프링과 테스트 통합
- @SpringBootTest : 스프링 부트 띄우고 테스트
- @Transactional : 매번 transaction을 rollback

## Exception 추가

## Item 
Item에서 stock에 대한 관리 기능

## Order 
- Order : order 생성, stock에 따른 취소, 전체 주문 가격 조회 
- OrderItem : 주문 대상이 되는 item 생성, 취소, 가격 조회
- 주문 검색 : jpql, criteria, queryDSL

## Web 
- Controller : Model에 객체를 담아 View로 전달(@GetMapping, @PostMapping)
- html form의 값을 Form class 객체에 담아 BindingResult로 검사후 저장
- @PathVariable
- @ModelAttribute
- @RequestParam

## dirty checking & merge
- 변경감지(prefered)
  - 필요한 속성만 변경 가능
  - 영속 엔티티를 수정하는 방법
  - transaction 내의 수정에 대한 commit 시의 update sql query 전송 
- merge
  - 모든 속성 변경 (지정해주지 않으면 null 값이 등록될 수 있다)
  - 준영속 엔티티를 영속 엔티티로
  - 1차 캐시에서 엔티티를 찾고 없으면 db에서 찾은 뒤 그 영속 엔티티에 준영속 엔티티의 값을 넣어준 후 1차 캐시에 저장 


## API 개발
- Entity를 직접 파라미터로 가져와 반환 x
- DTO를 두어 값을 담고 반환
- sample data : Service의 method으로 주문 관리, Member 등 생성 method 둠
- 데이터 조회 시
  - 엔티티 직접 노출 : api와 분리되지 않는 문제 
  - dto로 변환 : 1 + N + N 번 조회 (지연 로딩 두번, 영속성 컨텍스트에서 조회 -> 이미 조회한 경우를 생략)
  - fetch join
  - jpa dto 조회
  => dto로 변환 > fetch join > jpa > sql 순 활용
- collection 조회 시
   - 엔티티 직접 노출 : EAGER -> LAZY -> jackson 인식 x -> hibernate 활용 강제 지연 로딩
   - dto로 변환 : 1 + N의 문제 (ToMany, ToOne 조회 시)
   - dto + fetch join : paging 불가 (collection의 경우 불가, collection paging은 1개 가능)
   - paging 최적화 : ToOne fetch join + hibernate.default_batch_fetch_size , @BatchSize
   - dto jpa 직접 조회 : ToOne join -> ToMany(join시 row 증가) 따로 추가
   - dto jpa 직접 조회의 최적화 : ToMany는 Map으로
   - 
