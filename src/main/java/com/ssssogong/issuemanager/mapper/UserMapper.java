package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.FullUserDTO;
import com.ssssogong.issuemanager.dto.RegisterRequestDTO;
import com.ssssogong.issuemanager.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * User Entity - DTO 간 변환
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
    public final PasswordEncoder passwordEncoder;

    /**
     * RegisterRequestDTO -> User 엔티티<br>
     * 주의: password는 PasswordEncoder로 인코딩됨
     */
    public User toRegisterDTO(RegisterRequestDTO registerDTO) {
        return User.builder()
                .accountId(registerDTO.getAccountId())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();
    }

    /**
     * FullUserDTO -> User 엔티티<br>
     * 주의: password는 PasswordEncoder로 인코딩됨
     */
    public User toRegisterDTO(FullUserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .accountId(userDTO.getAccountId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
    }

    /**
     * User엔티티 -> UserDTO
     */
    public UserDTO toRegisterDTO(User user) {
        return FullUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .build();
    }

}
