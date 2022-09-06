package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(CallLogAspect.class)
@SpringBootTest
@Slf4j
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0; //프록시

    @Test
    void external() {
        //내부 함수 호출 시는 프록시가 아닌 타켓의 internal을 호출하므로 어드바이스를 적용못한다.
        callServiceV0.external();
        //log.info("target={}", callServiceV0.getClass()); //프록시임을 확인
    }

    @Test
    void internal() {
        //이것은 당연히 어드바이스를 적용한다
        callServiceV0.internal();
    }
}