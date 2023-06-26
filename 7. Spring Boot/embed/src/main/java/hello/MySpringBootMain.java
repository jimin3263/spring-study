package hello;

import hello.boot.MySpringApplication;
import hello.boot.MySpringBootApplication;

@MySpringBootApplication //현재 이 패키지로부터 컴포넌트 스캔한다
public class MySpringBootMain {

    public static void main(String[] args) {
        System.out.println("MySpringBootMain.main");
        //컴포넌트 스캔 기능을 쓴다 (config 파일)
        MySpringApplication.run(MySpringBootMain.class, args);
    }
}
