package hello.boot;

import hello.spring.HelloConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

public class MySpringApplication {
    public static void run(Class configClass, String[] args) { //args는 단순 값 전달
        System.out.println("MySpringApplication.run args=" + List.of(args));

        //톰캣 설정
        Tomcat tomcat = new Tomcat();
        //8080 연결
        Connector connector = new Connector();
        connector.setPort(8080);
        tomcat.setConnector(connector);

        //스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(configClass);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결 (컨트롤러 호출)
        DispatcherServlet dispatcher = new DispatcherServlet(applicationContext);

        //디스패처 서블릿 등록
        Context context = tomcat.addContext("", "/");
        tomcat.addServlet("", "dispatcher", dispatcher); //내장 톰캣에 디스패처 서블릿 등록
        context.addServletMappingDecoded("/", "dispatcher"); //컨테이너에 디스패처 서블릿 넣음

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
