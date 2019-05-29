package application.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface UserRepository extends JpaRepository<User, String> {

    List<User> findByActive(Boolean active);

    User findByLoginname(String loginname);

    User findByFullname(String loginname);

}
