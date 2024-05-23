package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequestDTO {
    private String accountId;
    private String password;
    private String username;
}
