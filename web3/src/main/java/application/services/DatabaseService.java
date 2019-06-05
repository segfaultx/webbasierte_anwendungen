package application.services;

import application.sichtung.SichtungsRepository;
import application.users.User;
import application.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DatabaseService {
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private SichtungsRepository sichtungsrepo;

    @Value("${fileupload.directory}")
    private String UPLOADDIR;

    // TODO bildservice auslagern
    public User addUser(User user, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(UPLOADDIR, "avatar-" + user.getLoginname() +
                    ".png");
            Files.copy(inputStream, filepath);
        }
        return userrepo.save(user);
    }

    public List<User> findAllUsers() {
        return userrepo.findAll();
    }

    public List<User> findAllUsersByOrderByLoginname() {
        return userrepo.findAllByOrderByLoginname();
    }

    public List<User> findAllUsersByLoginnameContainingOrFullnameContainingOrderByLoginname(String exp1, String exp2) {
        return userrepo.findAllByLoginnameContainingOrFullnameContainingOrderByLoginname(exp1, exp2);
    }

    public User findUserByLoginname(String name) {
        return userrepo.findByLoginname(name);
    }

    public List<User> findUsersByActive(Boolean activity) {
        return userrepo.findByActive(activity);
    }

    public void deleteUser(User user) throws IOException {
        Path path = Paths.get(UPLOADDIR, "avatar-" + user.getLoginname() + ".png");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        userrepo.delete(user);
    }
}
