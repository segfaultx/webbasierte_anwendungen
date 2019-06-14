package application.sichtung;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Comment findCommentByComment_idAndAndSichtung();

    public Comment findCommentByComment_idAndCreator();

    public Comment findCommentByComment_idAndCreatorAndSichtung();

    public List<Comment> findAllBySichtung();

    public List<Comment> findAllOrderByCreationDate();
}
