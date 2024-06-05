package org.pr1.securityservice.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pr1.securityservice.DTOs.UserRequestDTO;
import org.pr1.securityservice.DTOs.UserResponseDTO;
import org.pr1.securityservice.DTOs.UserUpdateDTO;
import org.pr1.securityservice.entities.User;
import org.pr1.securityservice.entities.UserRole;
import org.pr1.securityservice.mappers.UserMapper;
import org.pr1.securityservice.repositories.UserRepository;
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
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
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
                || userRequestDTO.getEmailAddress() == null || userRequestDTO.getUsername().isBlank() || userRequestDTO.getEmailAddress().isBlank()) {
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

    public Optional<UserResponseDTO> updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userUpdateDTO.getId()).orElseThrow(() -> new UsernameNotFoundException("user with the Id: " + userUpdateDTO.getId() + " was not found"));
        user.setRole(userUpdateDTO.getRole());
        user.setEnabled(userUpdateDTO.isEnabled());
        user.setUsername(userUpdateDTO.getUsername());
        user.setEmailAddress(userUpdateDTO.getEmailAddress());
        user.setAddress(userUpdateDTO.getAddress());
        User saved = userRepository.save(user);
        log.info("Saved user: {}", saved);
        return Optional.of(userMapper.mapUserToUserResponseDTO(saved));
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
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

    public Optional<UserResponseDTO> getUserById(Long userId) {
        return Optional.of(
                userMapper.mapUserToUserResponseDTO(userRepository.findById(userId)
                        .orElseThrow(() -> new UsernameNotFoundException("user with the Id: " + userId + " was not found")))
        );
    }

    public Optional<List<UserResponseDTO>> findAll() {
        return Optional.of(
                userRepository.findAll().stream().map(
                        userMapper::mapUserToUserResponseDTO
                ).toList()
        );
    }
}