package org.pr1.securityservice.controllers;

import lombok.RequiredArgsConstructor;
import org.pr1.securityservice.DTOs.LoginRequestDTO;
import org.pr1.securityservice.DTOs.LoginResponseDTO;
import org.pr1.securityservice.DTOs.RefreshTokenRequestDTO;
import org.pr1.securityservice.services.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class JWTController {
    private final JWTService jwtService;

    @PostMapping("/generate-token")
    public ResponseEntity<Optional<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<LoginResponseDTO> login = jwtService.login(loginRequestDTO);
        if (login.isEmpty()) {
            return new ResponseEntity<>(login, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> logout(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return null;
    }
}
