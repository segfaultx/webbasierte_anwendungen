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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@SessionAttributes("userlist")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userrepo;

    @Value("${fileupload.directory}")
    private String UPLOADDIR;

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
    public String addUser(@ModelAttribute("newUser") User newUser, BindingResult bindingResult, Model m, @RequestParam("picture") MultipartFile picture) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        if (userrepo.findByLoginname(newUser.getLoginname()) != null) {
            bindingResult.addError(new FieldError("newUser", "loginname", "#{adduser.usernametaken.error}"));
            m.addAttribute("newUser", newUser);
            return "adduser";
        }
        try {
            savePicture(newUser.getLoginname(), picture);
        } catch (IOException ex) {
            m.addAttribute("newUser", newUser);
            return "adduser";
        }

        newUser = userrepo.save(newUser);
        return "redirect:/users";
    }

    private void savePicture(String loginname, MultipartFile picture) throws IOException {

        InputStream inp = picture.getInputStream();
        Path filepath = Paths.get("/home/amatus/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/web3/src/main/java/application/users/uploads", "avatar-" + loginname +
                ".png");
        System.out.println(Files.exists(filepath));
        Files.copy(inp, filepath);
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
    public String updateUser(@ModelAttribute("oldUser") User oldUser, @ModelAttribute("editUser") User edittedUser, Model m, @RequestAttribute("picture") MultipartFile picture) {
        oldUser = edittedUser;
        try{
            savePicture(edittedUser.getLoginname(), picture);
        }catch(IOException ex){
            m.addAttribute("editUser", edittedUser);
            return "edituser";
        }
        userrepo.save(oldUser);
        m.addAttribute("userlist", userrepo.findAllByOrderByLoginname());
        return "userlist";


    }

    @PostMapping("/removeuser/{nr}")
    public String removeUser(@PathVariable("nr") int nr, Model m) {
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
