package com.koreait.facebook.user;

import com.koreait.facebook.user.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/login")
    public void login() {} /* void면 return없지만 비void면 String으로 */

    @GetMapping("/join")
    public void join(@ModelAttribute UserEntity userEntity){} /* Model 생략가능 */

    @PostMapping("/join")
    public String joinProc(UserEntity userEntity){
        service.join(userEntity);
        return "redirect:/login?email=1";
    }

    @GetMapping("/auth")
    public String auth(UserEntity param){
        int result = service.auth(param);
        return "redirect:needEmail?=" + result;
    }
}
