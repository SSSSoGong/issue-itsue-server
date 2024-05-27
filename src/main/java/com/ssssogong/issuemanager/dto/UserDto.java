package com.ssssogong.issuemanager.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class UserDto extends UserResponseDto {
    private Long id;
}
