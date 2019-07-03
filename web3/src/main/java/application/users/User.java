package application.users;

import application.sichtung.Comment;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Userclass, representing users from database
 */
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="loginname")
@Entity
@Table(name = "USERS")
@Validated
public class User implements Serializable {
    @Id
    @Size(min = 3, max = 80)
    private String loginname;
    @NotNull
    @JsonIgnore
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

    @JsonIgnore
    @OneToMany(mappedBy="creator", fetch=FetchType.EAGER)
    private List<Comment> commentList;

    /**
     * empty construtor for form purposes
     */
    public User() {

    }

    /**
     *
     * @param loginname
     * @param password
     * @param fullname
     */
    public User(String loginname, String password, String fullname) {
        this.loginname = loginname;
        this.password = password;
        this.fullname = fullname;
    }

    /**
     *
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @return
     */
    public String getFullname() {
        return fullname;
    }

    /**
     *
     * @return
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     *
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     *
     * @param loginname
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getUsergroup() {
        return usergroup;
    }

    /**
     *
     * @param usergroup
     */
    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @param commentList
     */
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    /**
     *
     * @return
     */
    public List<Comment> getCommentList() {
        return commentList;
    }
}
