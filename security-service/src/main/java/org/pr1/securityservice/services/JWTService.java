package org.pr1.securityservice.services;

import lombok.RequiredArgsConstructor;
import org.pr1.securityservice.DTOs.LoginRequestDTO;
import org.pr1.securityservice.DTOs.LoginResponseDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()
        );
        System.out.println(usernamePasswordAuthenticationToken);
        Authentication authenticate = authenticationManager.authenticate(
                usernamePasswordAuthenticationToken
        );
        System.out.println(authenticate);
        if (authenticate.isAuthenticated()){
            return generateToken(authenticate);
        }
        System.out.println("invalid cred");
//        throw new IllegalAccessException("invalid Credentials");
        return null;
    }

    private LoginResponseDTO generateToken(Authentication authenticate) {
        return null;
    }
}
