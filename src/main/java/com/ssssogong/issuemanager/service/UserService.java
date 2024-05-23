package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.FullUserDto;
import com.ssssogong.issuemanager.dto.RegisterRequestDto;
import com.ssssogong.issuemanager.dto.UserDto;
import com.ssssogong.issuemanager.exception.ConflictException;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.mapper.UserMapper;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import com.ssssogong.issuemanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserDto save(RegisterRequestDto registerRequest) throws Exception {
        // 예외 처리 : id가 존재하는 경우 기각
        if (userRepository.existsByAccountId(registerRequest.getAccountId())) {
            throw new ConflictException("user '" + registerRequest.getAccountId() + "' already exists.");
        }
        User user = userRepository.save(UserMapper.toUser(registerRequest));
        return UserMapper.toUserDTO(user);
    }

    public Object login() {
        return null;
    }

    /**
     * 해당 accountId의 회원을 삭제한다
     *
     * @param accountId 회원탈퇴할 회원의 로그인 id
     */
    public UserDto unregister(String accountId) throws Exception {
        Optional<User> target = userRepository.findByAccountId(accountId);
        if (target.isEmpty()) {
            throw new NotFoundException("user '" + accountId + "' does not exist");
        }
        userRepository.deleteById(target.get().getId());
        return UserMapper.toUserDTO(target.get());
    }

    public Collection<UserDto> findUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(user -> {
            userDtos.add(UserMapper.toUserDTO(user));
        });
        return userDtos;
    }

    public Collection<UserDto> findUsers(String username) {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAllByUsername(username).iterator().forEachRemaining(user -> {
            userDtos.add(UserMapper.toUserDTO(user));
        });
        return userDtos;
    }

    @Transactional
    public String findProjectRoleName(String accountId, Long projectId) {
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) throw new NotFoundException("user '" + accountId + "' not found");
        Optional<UserProject> optionalUserProject = userProjectRepository.findByUserIdAndProjectId(optionalUser.get().getId(), projectId);
        if (optionalUserProject.isEmpty()) throw new NotFoundException("user '" + accountId + "' doesn't belong to project");
        UserProject userProject = optionalUserProject.get();
        return userProject.getRole().getName();
    }

    public UserDto findUserByAccountId(String accountId) {
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) throw new NotFoundException("user '" + accountId + "' not found");
        return UserMapper.toUserDTO(optionalUser.get());
    }

    public FullUserDto updateUser(String accountId, RegisterRequestDto updateRequest) {
        // TODO: Update User
        // username과 password 수정
        User target = userRepository.findByAccountId(accountId).orElseThrow(NotFoundException::new);
        if(updateRequest.getUsername() != null && !updateRequest.getUsername().isBlank())
            target.setUsername(updateRequest.getUsername());
        if(updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank())
            target.setPassword(updateRequest.getPassword());
        User updated = userRepository.save(target);
        return UserMapper.toFullUserDTO(updated);
    }

    /**
     * 해당 User의 Authority (Role 및 Privilege)의 Collection을 반환한다.
     */
    @Transactional(readOnly = true)
    public Collection<GrantedAuthority> getAuthorities(final Long userId) {
        // UserProjectRepository에서 해당 User의 기록을 조회한다
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<UserProject> userProjects = userProjectRepository.findAllByUserIdFetchJoinProject(userId);
        for (UserProject userProject : userProjects) {
            // 각 Role에 지정된 Privilege를 가져온다.
            List<GrantedAuthority> auths = userProject.getRole().getGrantedAuthorities().stream().toList();
            for (GrantedAuthority g : auths) {
                // 각 PRIVILEGE를 PRIVILEGE_TO_PROJECT_{ID} 형태로 만들어 반환한다
                authorities.add(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return g.getAuthority() + "_TO_PROJECT_" + userProject.getProject().getId();
                    }
                });
            }
        }
        return authorities;
    }
}
