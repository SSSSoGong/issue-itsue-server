package com.ssssogong.issuemanager.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class FullUserDTO extends UserDTO {
    private String password;
}
