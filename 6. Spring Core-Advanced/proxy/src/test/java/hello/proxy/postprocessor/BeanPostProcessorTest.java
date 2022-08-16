package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {
    @Test
    void basicConfig() {
        //스프링 컨테이너에 직접 config 넣기
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorTest.BasicConfig.class);

        //beanA이름으로 객체 B로 등록된다.
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        //A는 빈으로 등록조차 되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A(); //스프링 빈 저장소에 저장하도록 한다
        }

        @Bean
        public AToBPostProcessor aToBPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName ={}, bean ={}", beanName, bean);
            if (bean instanceof A){
                return new B();
            }
            return bean;
        }
    }
}
