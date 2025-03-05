package dev.alvartaco.notifications.service.secure;

import dev.alvartaco.notifications.model.secure.User;
import dev.alvartaco.notifications.repository.secure.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);
    private final IUserRepository iUserRepository;

    public UserServiceImplementation(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserRepository.findByEmail(username);
        System.out.println(user);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with this email" + username);

        }

        log.info("#NOTIFICATIONS-D-C - Loaded user: {}, Role: {}", user.getEmail(), user.getRole());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}

