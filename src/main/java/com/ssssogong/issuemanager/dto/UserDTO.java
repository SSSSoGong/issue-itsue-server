package com.ssssogong.issuemanager.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class UserDTO extends UserResponseDTO {
    private Long id;
}
