package org.pr1.securityservice.DTOs;

import lombok.Data;
import org.pr1.securityservice.entities.UserRole;

import java.util.List;
@Data
public class UserUpdateDTO {
    private Long id;
    private String username;
    private String emailAddress;
    private boolean accountActivated;
    private String address;
    private List<UserRole> role;
    private boolean isEnabled;
}
