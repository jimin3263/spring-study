package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //method에 붙이는 에노터이션
@Retention(RetentionPolicy.RUNTIME) //실행시점에 애노테이션 읽을 수 있다.
public @interface MethodAop {
    String value();
}
