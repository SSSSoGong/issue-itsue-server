package com.ssssogong.issuemanager.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class UserResponseDto {
    private String accountId;
    private String username;
}
