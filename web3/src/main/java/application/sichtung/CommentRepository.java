package application.sichtung;

import application.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository matching comment object to the respective entities in the database
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 
     * @param id
     * @param creator
     * @return
     */
    public Comment findByCommentIdAndCreator(long id, User creator);

    /**
     * 
     * @param id
     * @param creator
     * @return
     */
    public Comment findCommentByCommentIdAndCreator(long id, User creator);

    /**
     * 
     * @param id
     * @param creator
     * @return
     */
    public Comment findCommentByCommentIdAndCreatorAndSichtung(long id, User creator, Sichtung sichtung);

    /**
     *
     * @param sichtung
     * @return
     */
    public List<Comment> findAllBySichtung(Sichtung sichtung);

    /**
     *
     * @param sichtung
     * @return
     */
    public List <Comment> findAllBySichtungOrderByCreationDateDesc(Sichtung sichtung);

    /**
     *
     * @return
     */
    public List<Comment> findAllByOrderByCreationDate();
}
