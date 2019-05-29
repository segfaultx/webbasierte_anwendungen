package application.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("userlist")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userrepo;

    @ModelAttribute("userlist")
    public void initUserlist(Model m) {
        m.addAttribute("userlist", userrepo.findAll());
    }

    @GetMapping
    public String showUserlist() {
        return "userlist";
    }

    @PostMapping("/adduser")
    @ResponseBody
    public String addUser() {
        return "Hallo!";
    }

    @GetMapping("/adduser")
    public String showAddUser() {
        return "adduser";
    }
}
