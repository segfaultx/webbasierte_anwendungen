package application.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import application.services.DatabaseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@SessionAttributes("userlist")
@RequestMapping("/users")
public class UserController {
    @Autowired
    DatabaseService dbservice;

    @Value("${fileupload.directory}")
    private String UPLOADDIR;


    @ModelAttribute("userlist")
    public void initUserlist(Model m) {
        m.addAttribute("userlist", dbservice.findAllUsers());
    }

    @GetMapping
    public String showUserlist(Model m, @ModelAttribute("userlist") List<User> userlist) {
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "userlist";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("newUser") User newUser, BindingResult bindingResult, Model m, @RequestParam("picture") MultipartFile picture) throws IOException {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        if (dbservice.findUserByLoginname(newUser.getLoginname()) != null) {
            bindingResult.addError(new FieldError("newUser", "loginname", "#{adduser.usernametaken.error}"));
            m.addAttribute("newUser", newUser);
            return "adduser";
        }
        if (picture.getSize() > 0) {
            dbservice.addUser(newUser, picture.getInputStream());
        } else {
            dbservice.addUser(newUser, null);
        }
        return "redirect:/users";
    }

    @GetMapping("/image/{name}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("name") String name) throws IOException {
        Path path = Paths.get(UPLOADDIR, "avatar-" + name + ".png");
        if (!Files.exists(path)) {
            path = Paths.get(UPLOADDIR, "avatar-default.png");
        }
        String mimetype = Files.probeContentType(path);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .header(null)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(mimetype))
                .body(resource);
    }

    @PostMapping
    public String filterUsers(Model m, @RequestParam("searchfield") String searchexp) {
        m.addAttribute("userlist", dbservice.findAllUsersByLoginnameContainingOrFullnameContainingOrderByLoginname(searchexp, searchexp));
        return "userlist";
    }

    @GetMapping("/edituser/{nr}")
    public String showEditUser(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("editUser", dbservice.findAllUsersByOrderByLoginname().get(nr));
        m.addAttribute("oldUser", dbservice.findAllUsersByOrderByLoginname().get(nr));
        return "edituser";
    }

    @PostMapping("/edituser")
    public String updateUser(@ModelAttribute("oldUser") User oldUser, @ModelAttribute("editUser") User edittedUser, Model m, @RequestAttribute("picture") MultipartFile picture) throws IOException {
        oldUser = edittedUser;
        if (picture.getSize() > 0) {
            dbservice.addUser(oldUser, picture.getInputStream());
        } else {
            dbservice.addUser(oldUser, null);
        }
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "userlist";


    }

    @PostMapping("/removeuser/{nr}")
    public String removeUser(@PathVariable("nr") int nr, Model m) throws IOException {
        dbservice.deleteUser(dbservice.findAllUsersByOrderByLoginname().get(nr));
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "redirect:/users";
    }

    @GetMapping("/adduser")
    public String showAddUser(Model m) {
        m.addAttribute("newUser", new User());
        return "adduser";
    }
}
