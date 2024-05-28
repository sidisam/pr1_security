package org.pr1.securityservice.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.pr1.securityservice.DTOs.UserRequestDTO;
import org.pr1.securityservice.DTOs.UserResponseDTO;
import org.pr1.securityservice.DTOs.UserUpdateDTO;
import org.pr1.securityservice.services.UserService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("/user")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<Optional<UserResponseDTO> > getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    @GetMapping()
    public ResponseEntity<Optional<List<UserResponseDTO>>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }
    @PostMapping()
    public ResponseEntity<Optional<UserResponseDTO>> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }
    @PutMapping()
    public ResponseEntity<Optional<UserResponseDTO>> updateUser(@RequestBody UserUpdateDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(userRequestDTO));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
