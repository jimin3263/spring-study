package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }
    //@GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model){

        if(memberId == null){
            return "home";
        }

        Member member = memberRepository.findById(memberId);
        if(member == null){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Long memberId, Model model){

        Member member = (Member) sessionManager.getSession(request);

        //세션이 존재하지 않으면
        if(member == null){
            return "home";
        }

        //세션 존재
        model.addAttribute("member", member);
        return "loginHome";

    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Long memberId, Model model){

        HttpSession session = request.getSession(false);//세션을 생성할 필요 없음

        //세션 없으면 Home
        if(session == null){
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(member == null){
            return "home";
        }

        //세션 존재하면 loginHome
        model.addAttribute("member", member);
        return "loginHome";

    }

    @GetMapping("/")
    public String homeLoginV4(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Long memberId, Model model){

        if(member == null){
            return "home";
        }

        //세션 존재하면 loginHome
        model.addAttribute("member", member);
        return "loginHome";

    }
}