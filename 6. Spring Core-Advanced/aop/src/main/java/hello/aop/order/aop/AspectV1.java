package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    @Around("execution(* hello.aop.order..*(..))")//포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws  Throwable { //Advice
        log.info("[log] {}", joinPoint.getSignature()); //join point 시그니처
        return joinPoint.proceed(); //실제 타겟 호출
    }
}
