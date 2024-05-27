package org.pr1.securityservice.DTOs;

import lombok.Data;
import org.pr1.securityservice.entities.UserRole;

import java.util.List;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String emailAddress;
    private boolean accountActivated;
    private String address;
    private List<UserRole> role;
    private boolean isEnabled;
}
