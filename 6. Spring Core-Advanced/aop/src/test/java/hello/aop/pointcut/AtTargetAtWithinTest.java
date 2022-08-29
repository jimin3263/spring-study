package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import hello.aop.pointcut.AtTargetAtWithinTest.Parent.Child;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
public class AtTargetAtWithinTest {
    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child Proxy={}", child.getClass());
        child.childMethod(); //부모 자식 모두 존재
        child.parentMethod(); //부모 클래스만 있는 메서드
    }

    static class Parent {
        public void parentMethod() {} //부모에만 있는 메서드

        @ClassAop
        static class Child extends Parent {
            public void childMethod() {}
        }
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {

        //@target: 부모 클래스의 메서드까지 적용
        //런타임시 판단 가능, 즉 프록시 필요 -> 적용 대상을 줄인 후 사용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

    }

    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }
        @Bean
        public Child child() {
            return new Child();
        }
        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }
}
