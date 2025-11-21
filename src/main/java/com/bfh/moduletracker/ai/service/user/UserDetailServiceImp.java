package com.bfh.moduletracker.ai.service.user;

import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.enums.Roles;
import com.bfh.moduletracker.ai.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailServiceImp.class);

    private final UserRepository userRepository;

    public UserDetailServiceImp(UserRepository userEntityRepository) {
        this.userRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Roles::name)
                        .toArray(String[]::new))
                .build();
    }

}
