package hello.boot;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented //말 그대로 문서화되도록!
@ComponentScan
public @interface MySpringBootApplication {
}
