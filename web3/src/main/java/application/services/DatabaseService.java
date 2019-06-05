package application.services;

import application.sichtung.SichtungsRepository;
import application.users.User;
import application.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private SichtungsRepository sichtungsrepo;

    public User addUser(User user) {
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

    public void deleteUser(User user) {
        userrepo.delete(user);
    }
}
