package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {
    
    @Test
    public void templateMethodV0() throws Exception{
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

    //비즈니스 로직을 제외하면 코드가 같다 -> 템플릿 메서드 패턴 이용해서 변하는 부분 변하지 않는 부분 분리해 적용 가능


    /**
     * 템플릿 메서드 패턴 적용
     */
    @Test
    public void templateMethodV1() throws Exception{
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    /**
     * 익명 내부 클래스 사용
     */
    @Test
    public void templateMethodV2() throws Exception{
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("클래스 이름1={}", template1.getClass()); //TemplateMethodTest$1 내부 클래스 이므로 $로 구분, 임의로 내부 클래스 명을 지정함
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("클래스 이름2={}", template2.getClass()); //TemplateMethodTest$2
        template2.execute();
    }
}
