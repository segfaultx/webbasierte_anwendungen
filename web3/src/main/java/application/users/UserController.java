package application.users;

import application.services.DatabaseService;
import application.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


/**
 * Controller handling all actions under /users
 */
@Controller
@SessionAttributes("userlist")
@RequestMapping("/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {
    @Autowired
    DatabaseService dbservice;

    @Autowired
    PictureService pictureservice;

    @Value("${avatar_fileupload.directory}")
    private String UPLOADDIR;

    /**
     *
     * @param m
     */
    @ModelAttribute("userlist")
    public void initUserlist(Model m) {
        m.addAttribute("userlist", dbservice.findAllUsers());
    }

    /**
     * shows userlist
     * @param m
     * @param userlist
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserlist(Model m, @ModelAttribute("userlist") List<User> userlist) {
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "users/userlist";
    }

    /**
     * adds new user to database
     * @param newUser
     * @param bindingResult
     * @param m
     * @param picture
     * @return
     * @throws IOException
     */
    @PostMapping("/adduser")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser(@Valid @ModelAttribute("newUser") User newUser, BindingResult bindingResult, Model m, @RequestParam("picture") MultipartFile picture) throws IOException {
        if (bindingResult.hasErrors()) {
            m.addAttribute("newUser", newUser);
            return "users/adduser";
        }
        if (dbservice.findUserByLoginname(newUser.getLoginname()) != null) {
            bindingResult.addError(new FieldError("newUser", "loginname", "#{adduser.usernametaken.error}"));
            m.addAttribute("newUser", newUser);
            return "users/adduser";
        }
        if (picture.getSize() > 0) pictureservice.saveUserAvatar(newUser.getLoginname(), picture.getInputStream());
        dbservice.addUser(newUser);
        return "redirect:/users";
    }

    /**
     * method to download image of user
     * @param name
     * @return
     * @throws IOException
     */
    @GetMapping("/image/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadImage(@PathVariable("name") String name) throws IOException {
        String mimetype = pictureservice.getMimeType(name);
        ByteArrayResource resource = pictureservice.loadUserAvatar(name);
        return ResponseEntity.ok()
                .header(null)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(mimetype))
                .body(resource);
    }

    /**
     * filter function
     * @param m
     * @param searchexp
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String filterUsers(Model m, @RequestParam("searchfield") String searchexp) {
        m.addAttribute("userlist", dbservice.findAllUsersByLoginnameContainingOrFullnameContainingOrderByLoginname(searchexp, searchexp));
        return "users/userlist";
    }

    /**
     * method to show details page to edit a user
     * @param nr
     * @param m
     * @param users
     * @return
     */
    @GetMapping("/edituser/{nr}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUser(@PathVariable("nr") int nr, Model m, @ModelAttribute("userlist") List<User> users) {
        User editUser = users.get(nr);
        m.addAttribute("editUser", users.get(nr));
        return "users/edituser";
    }

    /**
     * method to handle the post from editUser
     * @param oldUser
     * @param edittedUser
     * @param bindingResult
     * @param m
     * @param picture
     * @return
     * @throws IOException
     */
    @PostMapping("/edituser")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@ModelAttribute("oldUser")User oldUser, @ModelAttribute("editUser") User edittedUser, BindingResult bindingResult, Model m, @RequestAttribute("picture") MultipartFile picture) throws IOException {
        if(bindingResult.hasFieldErrors("fullname")){
            m.addAttribute("editUser", edittedUser);
            return "users/edituser";
        }
        if (picture.getSize() > 0) pictureservice.saveUserAvatar(edittedUser.getLoginname(), picture.getInputStream());
        User copyUser = dbservice.findUserByLoginname(edittedUser.getLoginname());
        copyUser.setFullname(edittedUser.getFullname());
        if(!edittedUser.getPassword().equals("")) copyUser.setPassword(edittedUser.getPassword());
        dbservice.addUser(copyUser);
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "redirect:/users";


    }

    /**
     * method to delete a user
     * @param nr
     * @param m
     * @return
     * @throws IOException
     */
    @PostMapping("/removeuser/{nr}")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeUser(@PathVariable("nr") int nr, Model m) throws IOException {
        pictureservice.removeUserAvatar(dbservice.findAllUsersByOrderByLoginname().get(nr).getLoginname());
        dbservice.deleteUser(dbservice.findAllUsersByOrderByLoginname().get(nr));
        m.addAttribute("userlist", dbservice.findAllUsersByOrderByLoginname());
        return "redirect:/users";
    }

    /**
     * method to show the add user screen
     * @param m
     * @return
     */
    @GetMapping("/adduser")
    public String showAddUser(Model m) {
        m.addAttribute("newUser", new User());
        return "users/adduser";
    }
}
