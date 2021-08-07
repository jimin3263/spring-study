package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSSION_COOKIE_NAME = "mySessionId";
    private Map<String,Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */

    public void createSession(Object value, HttpServletResponse response){
        //세션 id 생성, 값 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        //쿠키 생성
        Cookie cookie = new Cookie(SESSSION_COOKIE_NAME, sessionId);
        response.addCookie(cookie);
    }
    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies == null){
//            return null;
//        }
//
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals(SESSSION_COOKIE_NAME)){
//                return sessionStore.get(cookie.getValue());
//            }
//        }
//
//        return null;

        Cookie sessionCookie = findCookie(request, SESSSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }
        //sessionId 꺼내고 sessionId로 세션 가져옴
        return sessionStore.get(sessionCookie.getValue());

    }

    /**
     * 세션 만료
     */

    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);

    }
}
