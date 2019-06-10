package application.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = false)
public interface UserRepository extends JpaRepository<User, String> {

    @PreAuthorize("hasRole('ADMIN')")
    List<User> findByActive(Boolean active);
    @PreAuthorize("hasRole('ADMIN')")
    User findByLoginname(String loginname);
    @PreAuthorize("hasRole('ADMIN')")
    User findByFullname(String loginname);
    @PreAuthorize("hasRole('ADMIN')")
    List<User> findAllByOrderByLoginname();
    @PreAuthorize("hasRole('ADMIN')")
    List<User> findAllByLoginnameContainingOrFullnameContainingOrderByLoginname(String searchexp, String searchexp2);
}
