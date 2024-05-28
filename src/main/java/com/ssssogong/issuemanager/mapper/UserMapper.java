package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.config.PasswordEncoderConfig;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.FullUserDto;
import com.ssssogong.issuemanager.dto.RegisterRequestDto;
import com.ssssogong.issuemanager.dto.UserDto;
import com.ssssogong.issuemanager.dto.UserResponseDto;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * User Entity - DTO 간 변환
 */
@Component
@RequiredArgsConstructor
public class UserMapper {
    public static PasswordEncoder passwordEncoder;
    private final PasswordEncoder InjectedPasswordEncoder;

    @PostConstruct
    public void init() {
        passwordEncoder = InjectedPasswordEncoder;
    }

    /**
     * RegisterRequestDto -> User 엔티티<br>
     * 주의: password는 PasswordEncoder로 인코딩됨
     */
    public static User toUser(RegisterRequestDto registerDTO) {
        return User.builder()
                .accountId(registerDTO.getAccountId())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();
    }

    /**
     * FullUserDto -> User 엔티티<br>
     * 주의: password는 PasswordEncoder로 인코딩됨
     */
    public static User toUser(FullUserDto userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .accountId(userDTO.getAccountId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
    }

    public static UserDto toUserDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accountId(user.getAccountId())
                .build();
    }

    public static FullUserDto toFullUserDTO(User user){
        return FullUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accountId(user.getAccountId())
                .password(user.getPassword())
                .build();
    }

    public static UserResponseDto toUserResponseDTO(User user){
        return UserResponseDto.builder()
                .username(user.getUsername())
                .accountId(user.getAccountId())
                .build();
    }

    public static List<UserResponseDto> toUserResponseDTO(List<User> users){
        return users.stream()
                .map(UserMapper::toUserResponseDTO)
                .toList();
    }

    public static UserResponseDto toUserResponseDTO(UserDto userDto){
        return UserResponseDto.builder()
                .username(userDto.getUsername())
                .accountId(userDto.getAccountId())
                .build();
    }

}
