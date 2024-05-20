package com.ssssogong.issuemanager.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class UserResponseDTO {
    private String accountId;
    private String username;
}
