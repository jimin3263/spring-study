package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect //1) 자동 프록시 생성기가 Advisor를 찾아 프록시 생성한다. 2) @Aspect를 찾아 Advisor로 만들어 준다
public class LogTraceAspect {
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))") //포인트컷
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { //어드바이스
        TraceStatus status = null;
        try {
            //joinPoint: 실제 호출대상, 전달 인자, 어떤 객체, 어떤 메소드 호출되었는지
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}
