package org.pr1.securityservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pr1.securityservice.DTOs.LoginRequestDTO;
import org.pr1.securityservice.DTOs.LoginResponseDTO;

import org.pr1.securityservice.DTOs.RefreshTokenRequestDTO;
import org.pr1.securityservice.controllers.UserController;
import org.pr1.securityservice.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final UserController user;

    public Optional<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Fehlerhafte Login für user mit username: {}", loginRequestDTO.getUsername());
            return Optional.empty();
        }
        User user = (User) authenticate.getPrincipal();
        System.out.println(user) ;
        return Optional.of(generateTokens(authenticate.getName(),user.getId(), authenticate.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))));
    }

    public LoginResponseDTO generateTokens(String subject,long userId, String scope) {
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSetForIdToken = generateJwtClaim(subject,userId, instant, scope, 1, true);
        JwtClaimsSet jwtClaimsSetForRefreshToken = generateJwtClaim(subject,userId, instant, scope, 60, false);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setIdToken(jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetForIdToken)).getTokenValue());
        loginResponseDTO.setRefreshToken(jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetForRefreshToken)).getTokenValue());
        return loginResponseDTO;
    }

    public JwtClaimsSet generateJwtClaim(String subject,long userId, Instant instant, String scope, int expireIn, boolean withScope) {
        if (withScope)
            return JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(expireIn, ChronoUnit.MINUTES))
                    .claim("scope", scope)
                    .claim("id", userId)
                    .issuer("security-service")
                    .build();
        return JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(expireIn, ChronoUnit.MINUTES))
                .issuer("security-service")
                .build();
    }


    public Optional<LoginResponseDTO> generateTokenThrowRefreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Jwt decoded;
        try {
            decoded = jwtDecoder.decode(refreshTokenRequestDTO.getRefreshToken());
        } catch (JwtException e) {
            return Optional.empty();
        }
        String subject = decoded.getSubject();
        User userDetails = userService.loadUserByUsername(subject);
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return Optional.of(generateTokens(subject,userDetails.getId(), scope));
    }
}
