package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //class에 붙이는
@Retention(RetentionPolicy.RUNTIME)//실행할때까지 살아있는 어노테이션
public @interface ClassAop {
}
