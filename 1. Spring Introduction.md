# MVC 
: Model, View, Controller

## View
- 정적 컨텐츠
- 템플릿 엔진
  - 정적 컨텐츠와 달리 페이지 리소스 결과가 그대로 나오지 않고 viewResolver에서 엔진 처리함
  
## Controller
```java
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","hello");
        return "index";
    }
    
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }
    
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello-string"+name;
    }
```
- `@RequestParam`: 쿼리스트링으로 파라미터를 URL로 전송하고 controller에서 파라미터를 받을 때 사용
- `@ResponseBody`: viewResolver를 사용하지 않고 HTTP의 Body에 문자 내용을 직접 반환함

### Domain
: 비즈니스 도메인 객체(DB에 저장하고 관리됨)
### Repository
: DB에 접근, 도메인 객체를 DB에 저장하고 관리
```java
public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name); //Optional -> null이 될 수도 있는 객체를 감싸는 래퍼클래스
    List<Member> findAll();
}
```
### Service
: 핵심 비즈니스 로직 구현
- 회원 가입과 같은 비즈니스 로직 구현함

# Test
> Repository test
```java
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        //given
        Member member = new Member();
        member.setName("Spring");

        //when
        repository.save(member);
  
        //then
        Member result = repository.findById(member.getId()).get();
        //Assertions.assertEquals(member, result);
        assertThat(member).isEqualTo(result);
    }
```  

- `@AfterEach`: 한 번에 여러 테스트를 실행하면 직전 테스트 결과가 남을 수 있어서 오류가 발생할 수 있어, 각 테스트 종료할 때마다 메모리 DB에 저장된 데이터 삭제하도록 함
  - 의존적인 테스트는 좋지 않음 !

> Service test

```java
    @BeforeEach
    public void beforeEach(){
        repository = new MemoryMemberRepository();
        memberService = new MemberService(repository);
    }
    
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }
    
    @Test
    void 중복회원예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");
        
        //when
        memberService.join(member1);
        IllegalStateException e = Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        try {
//            memberService.join(member2);
//            fail("예외 발생해야 함");
//        }catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//
//        }
        
    }
```

- `@BeforeEach`: 같은 memoryMemberRepository가 사용되도록 함 

# 스프링 빈과 의존관계

> 컴포넌트 스캔과 자동 의존 관계 설정    
> - `@Component`를 포함하는 `@Service`, `@Controller`, `@Repository`는 스프링 빈으로 자동 등록됨   

```java
@Controller
public class MemberController {
    private final MemberService memberService = new MemberService();
}
```
- 여러 controller들이 Memberservice를 가져다 쓰는데 `new`로 새로운 인스턴스를 생성할 필요는 없음 => 하나 공유하면 됨
- spring container에 등록한 후 가져다 씀


```Java
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```
- `@Autowired`: 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어줌 => 의존관계를 외부에서 넣어주는 것(DI)
- 스프린 빈 등록 이미지
  - memberController -> memberService -> memberRepository
  
  
> 자바 코드로 직접 스프링 빈 등록   
> - 정형화 되지 않거나, 구현 클래스를 변경해야 할 때 사용  

```java
@Configuration
public class springConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```

# 스프링 DB 접근 기술

> 콘솔로 접근  
`cd h2`-> `cd bin` -> `./h2.sh`

## 순수 Jdbc

> build.gradle에 jdbc, h2 데이터베이스 관련 라이브러리 추가함  
```
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'com.h2database:h2'
```  

> resources/application.properties  
```
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
```

> JdbcMemberRepository 추가한 후, springConfig 수정
```java
@Configuration
public class springConfig {

    private DataSource dataSource;

    @Autowired
    public springConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
//      return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }
}
```
- 개방-폐쇄 원칙(= Open-Closed Principle)
  - 확장에는 열려있고 수정에는 닫혀있음
-  기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스 변경 가능

## JdbcTemplate
: 순수 Jdbc API 코드에서의 반복되는 부분을 대부분 제거해 줌

> JdbcTemplateMemberRepository  

```java
public class JdbcTemplateMemberRepository implements MemberRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id=?", memberRowMapper(),id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
```

- 생성자 하나일 때, `@Autowired` 생략 가능
- memberRowMapper() 함수로 회원 객체 생성하고 반환된 결과 값을 세팅한 후, 반환해 줌
- sql문은 직접 작성해야 함
- 이후, springConfig 파일에서 memberRepository() 생성자 수정  

## JPA
: 반복 코드, 기본적인 SQL도 JPA가 직접 만들어서 실행해줌

> build.gradle
```java
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```
- jdbc와 관련된 라이브러리도 포함

> resources/application.properties    
```java
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```
- ddl-auto=create
  - JPA가 테이블을 자동으로 생성
 
 > domain/Member
```java
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name="username")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
- `@Entity` 추가
- `@Id`: PK 설정
- `@GeneratedValue(strategy = GenerationType.IDENTITY)`: id값 자동 생성
- `@Column`: name 멤버 변수를 DB의 username과 mapping

> JpaMemberRepository

```java
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class,id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }
}
```
- findByName(): PK기반 함수가 아니기 때문에 다른 방법 사용
  - em.createQuery(): 회원 엔티티를 대상으로 쿼리를 보내, 해당하는 객체 자체를 검색함
- 트랜잭션 내에서 수행되도록 service에 `@Transactional`추가
  - 스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작, 메서드가 정상 종료되면 트랜잭션을 커밋함
  - 만약 런타임 예외가 발생하면 롤백!

## 스프링 데이터 JPA

```java
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,Long>, MemberRepository{

    @Override
    Optional<Member> findByName(String name);
}
```
- Member 클래스와 Pk type 명시함
- JpaRepository는 스프링 데이터 JPA에서 자동으로 인터페이스 구현체를 만들고, 빈으로 등록해 줌
- findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공

### 스프링 통합 테스트
`@SpringBootTest`: 스프링 컨테이너와 테스트를 함께 실행함  
`@Transactional`:  테스트 완료 후에 항상 롤백 -> 테스트에 했던 데이터가 실제 서버에 영향 미치지 않게 해줌

# AOP

: Aspect Oriendted Programming  
- **공통 관심사항**: 기능을 실행하는데 걸리는 시간을 측정하는 것과 같은 기능
- **핵심 관심사항**: 회원가입, 회원 조회와 같은 비즈니스 로직과 같은 기능  
*-> 분리하여 개발하는 기술*

```java
@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")
    
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString()+ " " + timeMs +"ms");
        }
    }
}
```
- `@Around`: 어떤 부분에 적용하고 싶은지 명시함
  - @Around("execution(* hello.hellospring.service..*(..))") 
- `@Component`: 스프링 빈으로 등록
- `@Aspect`: AOP 기능 
- AOP 적용 하면 프록시가 호출되고 joinPoint.proceed()가 호출될 때 실제 서비스의 메서드가 동작하게 됨
  

  
