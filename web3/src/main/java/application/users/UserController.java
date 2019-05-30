package application.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showUserlist(Model m, @ModelAttribute("userlist") List<User> userlist) {
        m.addAttribute("userlist", userrepo.findAllByOrderByLoginname());
        return "userlist";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("newUser") User newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        newUser = userrepo.save(newUser);
        return "redirect:/users";
    }

    @PostMapping("/users")
    public String filterUsers(Model m, @RequestParam("searchfield") String searchexp) {
        return "userlist";
    }

    @GetMapping("/adduser")
    public String showAddUser(Model m) {
        m.addAttribute("newUser", new User());
        return "adduser";
    }
}
