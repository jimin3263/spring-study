package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{
    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();//실제 객체를 실행하고 있음
        String decoResult = "*****" + result + "*****";
        log.info("꾸미기 적용 전 ={}, 적용 후 ={}", result, decoResult);
        return decoResult;
    }
}
