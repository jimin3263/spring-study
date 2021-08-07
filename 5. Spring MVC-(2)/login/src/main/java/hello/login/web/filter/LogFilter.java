package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    //먼저 호출
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        String uri = httpReq.getRequestURI();

        //요청 구분 위해서
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST[{}][{}]", uuid, request);
            chain.doFilter(request,response);
        } catch (Exception e){
            throw e;
        } finally {
            log.info("RESPONSE[{}][{}]", uuid, response);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
