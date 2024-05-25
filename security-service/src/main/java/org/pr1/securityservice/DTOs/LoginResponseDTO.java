package org.pr1.securityservice.DTOs;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String idToken;
    private String refreshToken;
}
