package org.pr1.securityservice.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.pr1.securityservice.DTOs.UserRequestDTO;
import org.pr1.securityservice.DTOs.UserResponseDTO;
import org.pr1.securityservice.entities.User;
import org.pr1.securityservice.entities.UserRole;
import org.pr1.securityservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user with username "+username+" was not found"));
    }
    @PostConstruct
    public void addingTestUsers(){
        userRepository.deleteAll();
        User user = User.builder()
               .role(List.of(UserRole.USER,UserRole.ADMIN))
                .username("testuser")
                .emailAddress("test@test.com")
                .password(passwordEncoder.encode("test"))
                .isEnabled(true)
                .build();
         userRepository.save(user);
    }

    public Optional<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        //check PA
        return  null;
    }

    public Optional<UserResponseDTO> updateUser(UserRequestDTO userRequestDTO) {
        return null;
    }
}