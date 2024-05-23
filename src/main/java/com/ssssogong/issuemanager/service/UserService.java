package com.ssssogong.issuemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.RegisterRequestDTO;
import com.ssssogong.issuemanager.dto.UserDTO;
import com.ssssogong.issuemanager.mapper.UserMapper;
import com.ssssogong.issuemanager.repository.AdminRepository;
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
    // temp
    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;

    public UserDTO save(RegisterRequestDTO registerRequest) throws Exception {
        // 예외 처리 : id가 존재하는 경우 기각
        if (userRepository.existsByAccountId(registerRequest.getAccountId())) {
            throw new Exception("user with id " + registerRequest.getAccountId() + " already exists.");
        }
        User user = userRepository.save(userMapper.toRegisterDTO(registerRequest));
        return userMapper.toRegisterDTO(user);
    }

    public Object login() {
        return null;
    }

    /**
     * 해당 accountId의 회원을 삭제한다
     *
     * @param accountId 회원탈퇴할 회원의 로그인 id
     */
    public UserDTO unregister(String accountId) throws Exception {
        Optional<User> target = userRepository.findByAccountId(accountId);
        if (target.isEmpty()) {
            throw new Exception("id does not exist");
        }
        userRepository.deleteById(target.get().getId());
        return userMapper.toRegisterDTO(target.get());
    }

    public Collection<UserDTO> findUsers() {
        List<UserDTO> userDTOs = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(user -> {
            userDTOs.add(userMapper.toRegisterDTO(user));
        });
        return userDTOs;
    }

    public Collection<UserDTO> findUsers(String username) {
        List<UserDTO> userDTOs = new ArrayList<>();
        userRepository.findAllByUsername(username).iterator().forEachRemaining(user -> {
            userDTOs.add(userMapper.toRegisterDTO(user));
        });
        return userDTOs;
    }

    @Transactional
    public String findProjectRoleName(String accountId, Long projectId) {
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) return null;
        Optional<UserProject> optionalUserProject = userProjectRepository.findByUserIdAndProjectId(optionalUser.get().getId(), projectId);
        if (optionalUserProject.isEmpty()) return null;
        UserProject userProject = optionalUserProject.get();
        return userProject.getRole().getName();
    }

    public UserDTO findUserByAccountId(String accountId) {
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) return null;
        return userMapper.toRegisterDTO(optionalUser.get());
    }

    public UserDTO updateUser() {
        // TODO: Update User
        return null;
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

    public Map<String, String> adminLogin(RegisterRequestDTO requestDTO) {
        String id = requestDTO.getAccountId();
        String pw = requestDTO.getPassword();
        Optional<Admin> _admin = adminRepository.findByAccountId(id);
        if (_admin.isEmpty()) return null;
        Admin admin = _admin.get();
        if (admin.getPassword().equals(userMapper.passwordEncoder.encode(pw))) {
            String token = jwtUtil.createToken(id, true);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> jwtBody = new HashMap<>();
            jwtBody.put("authorization", "Bearer " + token);
            return jwtBody;
        }
        return null;
    }
}
