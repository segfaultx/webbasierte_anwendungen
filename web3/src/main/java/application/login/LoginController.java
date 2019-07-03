package application.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for /login
 */
@Controller
public class LoginController {
    @Autowired
    PasswordEncoder pwenc;

    /**
     *
     * @return
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login/login";
    }

    /**
     *
     * @return
     */
    @GetMapping("/")
    public String swapToLogin(){ return "redirect:/login";}
}