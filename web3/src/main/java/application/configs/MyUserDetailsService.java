package application.configs;

import application.services.DatabaseService;
import application.sichtung.SichtungsController;
import application.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private DatabaseService dbservice;
    @Autowired
    PasswordEncoder pwenc;

    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dbservice.findUserByLoginname(username);
        logger.info("Extracted User: " + user);
        if (user == null) throw new UsernameNotFoundException(username);
        return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword()).roles(user.getUsergroup()).build();
    }
}
