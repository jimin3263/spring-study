package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic {
    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation() {
        log.info("Time Decorator 실행");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation(); //실제 실행

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("Time Decorator 종료 result Time ={}ms", resultTime);
        return result;
    }
}
