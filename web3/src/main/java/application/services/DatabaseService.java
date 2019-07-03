package application.services;

import application.sichtung.*;
import application.users.User;
import application.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Database service class, handling all database actions
 */
@Service
public class DatabaseService {
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private SichtungsRepository sichtungsrepo;
    @Autowired
    PasswordEncoder pwenc;
    @Autowired
    private CommentRepository commentRepository;

    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    /**
     * method to add new user to db
     * @param user
     * @return
     */
    public User addUser(User user) {
        user.setUsergroup("MEMBER");
        user.setPassword(pwenc.encode(user.getPassword()));
        return userrepo.save(user);
    }

    /**
     *
     * @return
     */
    public List<User> findAllUsers() {
        return userrepo.findAll();
    }

    /**
     *
     * @return
     */
    public List<User> findAllUsersByOrderByLoginname() {
        return userrepo.findAllByOrderByLoginname();
    }

    /**
     * searches for user containing either exp1 or exp2 in fullname or loginname
     * @param exp1
     * @param exp2
     * @return
     */
    public List<User> findAllUsersByLoginnameContainingOrFullnameContainingOrderByLoginname(String exp1, String exp2) {
        return userrepo.findAllByLoginnameContainingOrFullnameContainingOrderByLoginname(exp1, exp2);
    }

    /**
     * finds user by username
     * @param name
     * @return
     */
    public User findUserByLoginname(String name) {
        User usr = userrepo.findByLoginname(name);
        logger.info("USER EXTRACTED: " + usr);
        return userrepo.findByLoginname(name);
    }

    /**
     *
     * @param activity
     * @return
     */
    public List<User> findUsersByActive(Boolean activity) {
        return userrepo.findByActive(activity);
    }

    /**
     *
     * @param user
     */
    public void deleteUser(User user) {
        userrepo.delete(user);
    }

    /**
     * adds new sighting to db
     * @param sichtung
     * @return
     */
    public Sichtung saveSichtung(Sichtung sichtung) {
        return sichtungsrepo.save(sichtung);
    }

    /**
     *
     * @return
     */
    public List<Sichtung> findAllSichtungen() {
        return sichtungsrepo.findAll();
    }

    /**
     *
     * @param date
     * @return
     */
    public List<Sichtung> findAllSichtungenByDate(LocalDateTime date) {
        return sichtungsrepo.findByDate(date);
    }

    /**
     *
     * @param sichtung
     */
    public void deleteSichtung(Sichtung sichtung) {
        sichtungsrepo.delete(sichtung);
    }

    /**
     *
     * @param id
     * @return
     */
    public Sichtung findSichtungByID(long id) {
        return sichtungsrepo.getOne(id);
    }

    /**
     * adds new comment to db
     * @param comment
     * @return
     */
    public Comment addComment(Comment comment){
        return commentRepository.save(comment);
    }

    /**
     *
     * @param comment
     */
    public void removeComment(Comment comment){
        commentRepository.delete(comment);
    }

    /**
     *
     * @param id
     * @return
     */
    public Comment findCommentByID(long id){
        return commentRepository.getOne(id);
    }

    /**
     *
     * @param sichtung
     * @return
     */
    public List<Comment> findCommentsBySichtungOrderByDateDesc(Sichtung sichtung){
        return commentRepository.findAllBySichtungOrderByCreationDateDesc(sichtung);
    }

    /**
     *
     * @param id
     */
    public void deleteCommentById(long id){
        commentRepository.deleteById(id);
    }
}
