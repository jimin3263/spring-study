package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDiAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) //JDK 동적 프록시 (인터페이스)
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})
@SpringBootTest //CGLIB가 기본으로 적용된다.
@Import(ProxyDiAspect.class)
public class ProxyDiTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl; //빈에서는 인터페이스를 구현한 프록시가 등록, 타입 예외 발생

    @Test
    void go() {
        log.info("memberService class ={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }

}
