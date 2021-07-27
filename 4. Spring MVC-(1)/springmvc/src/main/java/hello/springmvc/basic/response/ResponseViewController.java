package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    //ModelAndView 사용
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data","hello!");

        return mav;
    }

    //string 반환
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data","뇽안");
        return "response/hello";
    }

    //컨트롤러의 요청, 뷰의 논리 이름이 같으면 뷰 반환
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!");
    }
}
