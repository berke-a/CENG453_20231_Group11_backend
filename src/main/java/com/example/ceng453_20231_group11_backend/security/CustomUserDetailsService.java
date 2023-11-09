package com.example.ceng453_20231_group11_backend.security;

import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<com.example.ceng453_20231_group11_backend.entity.User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("User not found by name: %s", username));
        }
        return toUserDetails(user.get());
    }

    private UserDetails toUserDetails(com.example.ceng453_20231_group11_backend.entity.User user) {
        /* withDefaultPasswordEncoder hashes the password, creates authority list and returns a valid User */
        return User.withDefaultPasswordEncoder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
