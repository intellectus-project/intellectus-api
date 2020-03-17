package com.atixlabs.security;

import com.atixlabs.model.configuration.User;
import com.atixlabs.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> user = userService.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username or email : " + username);
        }

        return UserPrincipal.create(user.get());
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userService.findById(id);

        return UserPrincipal.create(user);
    }
}
