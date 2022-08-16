package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {
    //@Bean
    public Advisor advisor1(LogTrace logTrace) {
        //어디에
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        //어떤 로직을
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    //@Bean
    public Advisor advisor2(LogTrace logTrace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        //* 모든 반환 타입
        //app 하위와 하위의 모든 패키지, 프록시 적용 대상
        //* 모든 메서드 이름, (..) 파라미터는 상관없다
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        //noLog 메서드는 제외
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
