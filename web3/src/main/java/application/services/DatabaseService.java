package application.services;

import application.sichtung.Sichtung;
import application.sichtung.SichtungsController;
import application.sichtung.SichtungsRepository;
import application.users.User;
import application.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseService {
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private SichtungsRepository sichtungsrepo;
    @Autowired
    PasswordEncoder pwenc;

    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    public User addUser(User user) {
        user.setUsergroup("MEMBER");
        user.setPassword(pwenc.encode(user.getPassword()));
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
        User usr = userrepo.findByLoginname(name);
        logger.info("USER EXTRACTED: " + usr);
        return userrepo.findByLoginname(name);
    }

    public List<User> findUsersByActive(Boolean activity) {
        return userrepo.findByActive(activity);
    }

    public void deleteUser(User user) {
        userrepo.delete(user);
    }

    public Sichtung saveSichtung(Sichtung sichtung) {
        return sichtungsrepo.save(sichtung);
    }

    public List<Sichtung> findAllSichtungen() {
        return sichtungsrepo.findAll();
    }

    public List<Sichtung> findAllSichtungenByDate(LocalDateTime date) {
        return sichtungsrepo.findByDate(date);
    }

    public void deleteSichtung(Sichtung sichtung) {
        sichtungsrepo.delete(sichtung);
    }
}
