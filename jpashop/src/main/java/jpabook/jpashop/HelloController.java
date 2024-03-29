package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") //url을 hello로 받은 경우
    public String hello(Model model){
        //data key를 가진 hello value를 model로 넘김
        model.addAttribute("data", "hello");
        return "hello"; //hello.html
    }
}
