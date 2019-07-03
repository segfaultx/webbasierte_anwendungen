package application.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface for User repository implementations
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     *
     * @param active
     * @return
     */
    List<User> findByActive(Boolean active);

    /**
     *
     * @param loginname
     * @return
     */
    User findByLoginname(String loginname);

    /**
     *
     * @param loginname
     * @return
     */
    User findByFullname(String loginname);

    /**
     *
     * @return
     */
    List<User> findAllByOrderByLoginname();

    /**
     *
     * @param searchexp
     * @param searchexp2
     * @return
     */
    List<User> findAllByLoginnameContainingOrFullnameContainingOrderByLoginname(String searchexp, String searchexp2);
}
