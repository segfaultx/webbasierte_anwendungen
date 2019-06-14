package application.sichtung;

import application.users.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long comment_id;
    private String message;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Sichtung sichtung;
}