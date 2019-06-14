package application.sichtung;

import application.users.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long comment_id;
    @NotNull
    @Size(min=2, max=80)
    private String message;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Sichtung sichtung;

    @NotNull
    private String fullname;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    public long getComment_id() {
        return comment_id;
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
        this.comment_id = comment_id;
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