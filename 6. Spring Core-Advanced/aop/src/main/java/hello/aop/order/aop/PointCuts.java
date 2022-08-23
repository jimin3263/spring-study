package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {
    //hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder(){} //포인트컷 시그니처

    //클래스 이름 패턴이 *Service (인터페이스에도 적용 가능)
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService(){}

    //조합 가능
    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}

}
