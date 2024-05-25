package org.pr1.securityservice.controllers;

import lombok.RequiredArgsConstructor;
import org.pr1.securityservice.DTOs.LoginRequestDTO;
import org.pr1.securityservice.DTOs.LoginResponseDTO;
import org.pr1.securityservice.DTOs.RefreshTokenRequestDTO;
import org.pr1.securityservice.services.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JWTController {
    private final JWTService jwtService;
    private AuthenticationManager authenticationManager;
    @PostMapping("/generate-token")
    public ResponseEntity<LoginResponseDTO>  login(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println(loginRequestDTO);
        return ResponseEntity.ok(jwtService.login(loginRequestDTO));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> logout(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return null;
    }
}
