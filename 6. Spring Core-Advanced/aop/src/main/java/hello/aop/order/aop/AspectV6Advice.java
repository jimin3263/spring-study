package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV6Advice {
    //메서드 실행 전 후 작업 실행
    //proceed 재시도도 가능
    @Around("hello.aop.order.aop.PointCuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable { //ProceedingJoinPoint는 Around 에서만 사용
        try {
            //@Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            //@AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally { //리소스 정리 -> 정상 종료 or catch 하든 호출되는 블럭
            //@After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    //@Around와 다르게 작업 흐름을 변경할 수 없다, proceed()를 호출할 필요가 없다
    //제약 덕분에 역할이 명확해질 수 있다.
    @Before("hello.aop.order.aop.PointCuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[Before] {}", joinPoint.getSignature()); //함수 실행 직전까지만
        //타겟이 알아서 호출된다
    }

    //메서드 실행이 정상적으로 반환될 때 실행
    //return 타입이 일치할 때만 호출된다
    @AfterReturning(value = "hello.aop.order.aop.PointCuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[Return] {} return={}", joinPoint.getSignature(), result);
        //결과값을 받을 수 있다.
    }

    //부모타입 예외를 지정하면 자식 예외에 적용
    @AfterThrowing(value = "hello.aop.order.aop.PointCuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex){
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "hello.aop.order.aop.PointCuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[After] {}", joinPoint.getSignature());
    }
}
