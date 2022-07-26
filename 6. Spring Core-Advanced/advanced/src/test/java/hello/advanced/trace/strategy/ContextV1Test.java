package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    public void strategyV0() throws Exception{
        logic1();
        logic2();
    }

    private void logic1(){
        //시간을 측정하는 부분, 비즈니스 로직 실행 부분이 동시에 존재
        //템플릿 메서드 패턴을 이용해 변하는, 변하지 않는 부분을 분리해본다.

        long startTime = System.currentTimeMillis();

        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2(){
        long startTime = System.currentTimeMillis();

        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /**
     * 전략 패턴 적용
     */
    @Test
    public void strategyV1() throws Exception{
        //전략을 생성자로 주입
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    @Test
    public void strategyV2(){
        //익명 클래스 사용
        Strategy strategyLogic1 = new Strategy(){
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };

        ContextV1 contextV1 = new ContextV1(strategyLogic1);
        contextV1.execute();

        Strategy strategyLogic2 = new Strategy(){
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };

        ContextV1 contextV2 = new ContextV1(strategyLogic2);
        contextV2.execute();
    }

    @Test
    public void strategyV3(){
        //생성자 주입시 인스턴스 생성
        ContextV1 contextV1 = new ContextV1(new Strategy(){
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        contextV1.execute();


        ContextV1 contextV2 = new ContextV1(new Strategy(){
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        contextV2.execute();
    }

    @Test
    public void strategyV4() throws Exception{
        //람다식 사용, 인터페이스의 메서드 하나일 때 사용 가능 ~
        ContextV1 contextV1 = new ContextV1(() -> log.info("비즈니스 로직 실행"));
        contextV1.execute();
    }

}
