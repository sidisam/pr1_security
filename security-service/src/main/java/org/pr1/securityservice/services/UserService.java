package org.pr1.securityservice.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pr1.securityservice.DTOs.UserRequestDTO;
import org.pr1.securityservice.DTOs.UserResponseDTO;
import org.pr1.securityservice.entities.User;
import org.pr1.securityservice.entities.UserRole;
import org.pr1.securityservice.mappers.UserMapper;
import org.pr1.securityservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user with username " + username + " was not found"));
    }

    @PostConstruct
    public void addingTestUsers() {
        userRepository.deleteAll();
        User user = User.builder()
                .role(List.of(UserRole.USER, UserRole.ADMIN))
                .username("testuser")
                .emailAddress("test@test.com")
                .password(passwordEncoder.encode("test"))
                .isEnabled(true)
                .build();
        userRepository.save(user);
    }

    public Optional<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null || userRequestDTO.getUsername() == null
                || userRequestDTO.getEmailAddress() == null
                || userRequestDTO.getPassword() == null) {
            throw new IllegalArgumentException("Invalid input");
        }
        if (!passwordIsValid(userRequestDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User user = userMapper.mapRequestDTOToUser(userRequestDTO);
        log.info("user to save: {}", user);
        User savedUser = userRepository.save(user);
        log.info("saved user: {}", savedUser);
        UserResponseDTO userResponseDTO = userMapper.mapUserToUserResponseDTO(savedUser);
        log.info("saved user after mapping : {}", userResponseDTO);
        return Optional.of(userResponseDTO);
    }

    public Optional<UserResponseDTO> updateUser(UserRequestDTO userRequestDTO) {
        return null;
    }

    public boolean passwordIsValid(String password) {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,200}$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(password);
        return matcher.matches();
    }
}