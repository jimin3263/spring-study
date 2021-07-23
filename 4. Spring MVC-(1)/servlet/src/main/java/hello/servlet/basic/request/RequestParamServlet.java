package hello.servlet.basic.request;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //전체
        System.out.println("[전체 파라미터 조회] - start");

        request.getParameterNames().asIterator()
                .forEachRemaining(parametername -> System.out.println(parametername + "=" + request.getParameter(parametername)));

        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        //단일
        System.out.println("[단일 파라미터 조회] - start");
        System.out.println(request.getParameter("name"));
        System.out.println(request.getParameter("age"));
        System.out.println("[단일 파라미터 조회] - end");
        System.out.println();

        //복수 파라미터
        System.out.println("[이름이 같은 복수 파라미터 조회] - start");
        String[] values = request.getParameterValues("name");
        for (String value : values) {
            System.out.println("username =  "+ value);
        }
        System.out.println("[이름이 같은 복수 파라미터 조회] - end");
        System.out.println();

        //map 조회
        System.out.println("[map으로 조회] - start");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(String key : parameterMap.keySet()){
            String[] strings = parameterMap.get(key);
            for (String value2 : strings) {
                System.out.println(key+" : "+value2);
            }
        }
        System.out.println("[map으로 조회] - end");
        response.getWriter().write("ok");

    }
}
