package org.pr1.securityservice.DTOs;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
