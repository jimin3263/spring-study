package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){} //포인트컷 시그니처

    //클래스 이름 패턴이 *Service (인터페이스에도 적용 가능)
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService(){}

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws  Throwable { //Advice
        log.info("[log] {}", joinPoint.getSignature()); //join point 시그니처
        return joinPoint.proceed(); //실제 타겟 호출
    }

    //hello.aop.order 패키지와 하위 패키지이면서 동시에 클래스 이름이 *Service인 것
    // &&, ||, ! 사용 가능
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally { //리소스 정리 -> 정상 종료 or catch 하든 호출되는 블럭
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }
}
