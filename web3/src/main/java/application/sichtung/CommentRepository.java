package application.sichtung;

import application.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Comment findByCommentIdAndCreator(long id, User creator);

    public Comment findCommentByCommentIdAndCreator(long id, User creator);

    public Comment findCommentByCommentIdAndCreatorAndSichtung(long id, User creator, Sichtung sichtung);

    public List<Comment> findAllBySichtung(Sichtung sichtung);

    public List <Comment> findAllBySichtungOrderByCreationDateDesc(Sichtung sichtung);

    public List<Comment> findAllByOrderByCreationDate();
}
