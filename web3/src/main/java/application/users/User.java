package application.users;

import application.sichtung.Comment;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="loginname")
@Entity
@Table(name = "USERS")
@Validated
public class User implements Serializable {
    @Id
    @Size(min = 3, max = 80)
    private String loginname;
    @NotNull
    @Size(min = 3, max = 80)
    private String password;
    @NotNull
    @Size(min = 3, max = 80)
    private String fullname;
    @NotNull
    private Boolean active = false;
    @GeneratedValue
    private long id;
    private String usergroup;

    @OneToMany(mappedBy="creator", fetch=FetchType.EAGER)
    private List<Comment> commentList;

    public User() {

    }

    public User(String loginname, String password, String fullname) {
        this.loginname = loginname;
        this.password = password;
        this.fullname = fullname;
    }

    public Boolean getActive() {
        return active;
    }

    public String getFullname() {
        return fullname;
    }

    public String getLoginname() {
        return loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }
}
