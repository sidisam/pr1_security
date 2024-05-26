package org.pr1.securityservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pr1.securityservice.DTOs.LoginRequestDTO;
import org.pr1.securityservice.DTOs.LoginResponseDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
        return Optional.of(generateToken(authenticate));
    }

    public LoginResponseDTO generateToken(Authentication authenticate) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        String subject = authenticate.getName();

        String scope = authenticate.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSetForIdToken = generateJwtClaim(subject, instant, scope, 30);
        JwtClaimsSet jwtClaimsSetForRefreshToken = generateJwtClaim(subject, instant, scope, 60);
        loginResponseDTO.setIdToken(jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetForIdToken)).getTokenValue());
        loginResponseDTO.setRefreshToken(jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetForRefreshToken)).getTokenValue());
        return loginResponseDTO;
    }

    public JwtClaimsSet generateJwtClaim(String subject, Instant instant, String scope, int expireIn) {
        return JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(expireIn, ChronoUnit.MINUTES))
                .issuer("security-service")
                .claim("scope", scope)
                .build();
    }
}
