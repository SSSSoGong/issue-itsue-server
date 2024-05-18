package com.ssssogong.issuemanager.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String accountId;
    private String password;
    private String username;
}
