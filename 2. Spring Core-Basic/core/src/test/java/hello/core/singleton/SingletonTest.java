package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();

        MemberService memberService1 = appConfig.memberService();
        MemberService memberService2 = appConfig.memberService();

        //참조값이 다름
        System.out.println("memberService1= " + memberService1);
        System.out.println("memberService2= " + memberService2);

        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

    }

    @Test
    @DisplayName("싱글톤 패턴을 사용한 객체 사용")
    void singleTonServiceTest(){
        SingleTonService singleTonService1 = SingleTonService.getInstance();
        SingleTonService singleTonService2 = SingleTonService.getInstance();

        //참조값이 같
        System.out.println("memberService1= " + singleTonService1);
        System.out.println("memberService2= " + singleTonService2);

        Assertions.assertThat(singleTonService1).isSameAs(singleTonService2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 같
        System.out.println("memberService1= " + memberService1);
        System.out.println("memberService2= " + memberService2);

        Assertions.assertThat(memberService1).isSameAs(memberService2);


    }
}
