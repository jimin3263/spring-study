package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV2;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {
    /**
     *  전략 패턴 메서드에 파라미터로 넘기는 방식
     */
    @Test
    public void strategyV1() throws Exception {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1()); //실행시점에 원하는 전략을 전달한다
        context.execute(new StrategyLogic2());
    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    public void strategyV2() throws Exception {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
    }

    /**
     * 람다로 변환
     */
    @Test
    public void strategyV3() throws Exception {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
    }
}
