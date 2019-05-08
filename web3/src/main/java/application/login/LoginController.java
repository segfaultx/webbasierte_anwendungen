package application.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController{
    
    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }
    @PostMapping("/login")
    public String checkLogin(@RequestParam("username")String uname, @RequestParam("password")String pw, Model m){
        if(pw.equals(uname+"!")){
            m.addAttribute("uname", uname);
            return "sichtungen";
        }
        return "login";
    }
}