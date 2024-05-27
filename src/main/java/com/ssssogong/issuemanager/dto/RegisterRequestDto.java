package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequestDto {
    private String accountId;
    private String password;
    private String username;
}
