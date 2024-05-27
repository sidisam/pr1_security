package org.pr1.securityservice.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pr1.securityservice.DTOs.UserRequestDTO;
import org.pr1.securityservice.DTOs.UserResponseDTO;
import org.pr1.securityservice.entities.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User mapRequestDTOToUser(UserRequestDTO userRequestDTO){
        return modelMapper.map(userRequestDTO,User.class);
    }
    public UserResponseDTO mapUserToUserResponseDTO(User user){
        return modelMapper.map(user,UserResponseDTO.class);
    }
}
