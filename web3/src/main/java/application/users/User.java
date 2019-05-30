package application.users;

import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
@Validated
public class User {
    @Id
    private String loginname;
    @NotNull
    private String password;
    @NotNull
    private String fullname;
    @NotNull
    private Boolean active = false;

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
}
