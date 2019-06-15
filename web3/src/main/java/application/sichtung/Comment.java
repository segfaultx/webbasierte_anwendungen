package application.sichtung;

import application.users.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Validated
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private long commentId;
    @NotNull
    @Size(min=2, max=80)
    private String message;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User creator;
    @ManyToOne(fetch=FetchType.EAGER)
    @NotNull
    private Sichtung sichtung;

    @NotNull
    private String fullname;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    public long getCommentId() {
        return commentId;
    }

    public Sichtung getSichtung() {
        return sichtung;
    }

    public String getMessage() {
        return message;
    }

    public User getCreator() {
        return creator;
    }

    public void setComment_id(long comment_id) {
        this.commentId = comment_id;
    }

    public void setCreator(User creator) {
        this.fullname = creator.getFullname();
        this.creator = creator;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSichtung(Sichtung sichtung) {
        this.sichtung = sichtung;
    }

    public String getFullname() {
        return fullname;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}