package application.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public String addUser(@ModelAttribute("newUser") User newUser, BindingResult bindingResult, Model m) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        if (userrepo.findByLoginname(newUser.getLoginname()) != null) {
            bindingResult.addError(new FieldError("newUser", "loginname", "#{adduser.usernametaken.error}"));
            m.addAttribute("newUser", newUser);
            return "adduser";
        }
        newUser = userrepo.save(newUser);
        return "redirect:/users";
    }

    @PostMapping
    public String filterUsers(Model m, @RequestParam("searchfield") String searchexp) {
        m.addAttribute("userlist", userrepo.findAllByLoginnameContainingOrFullnameContainingOrderByLoginname(searchexp, searchexp));
        return "userlist";
    }

    @GetMapping("/edituser/{nr}")
    public String showEditUser(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("editUser", userrepo.findAllByOrderByLoginname().get(nr));
        m.addAttribute("oldUser", userrepo.findAllByOrderByLoginname().get(nr));
        return "edituser";
    }
    @PostMapping("/edituser")
    public String updateUser(@ModelAttribute("oldUser") User oldUser, @ModelAttribute("editUser") User edittedUser, Model m){
        oldUser = edittedUser;
        userrepo.save(oldUser);
        m.addAttribute("userlist", userrepo.findAllByOrderByLoginname());
        return "userlist";


    }

    @PostMapping("/removeuser/{nr}")
    public String removeUser(@PathVariable("nr") int nr, Model m){
        userrepo.delete(userrepo.findAllByOrderByLoginname().get(nr));
        m.addAttribute("userlist", userrepo.findAllByOrderByLoginname());
        return "redirect:/users";
    }
    @GetMapping("/adduser")
    public String showAddUser(Model m) {
        m.addAttribute("newUser", new User());
        return "adduser";
    }
}
