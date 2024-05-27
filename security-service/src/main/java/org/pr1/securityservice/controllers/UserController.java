package org.pr1.securityservice.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @GetMapping("/test")
    @PreAuthorize("hasAnyAuthority('SCOPE_MANAGER')")
    public String getTestDAta(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return "User is successfully logged in";
    }

}
