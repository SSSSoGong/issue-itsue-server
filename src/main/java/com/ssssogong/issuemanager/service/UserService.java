package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.RegisterRequestDTO;
import com.ssssogong.issuemanager.dto.UserDTO;
import com.ssssogong.issuemanager.dto.UserResponseDTO;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO register(RegisterRequestDTO registerRequest){
        return null;
    }

    public Object login(){
        return null;
    }

    public UserDTO unregister(String name){
        return null;
    }

    public Collection<UserDTO> findUsers(){
        return null;
    }

    public Collection<UserDTO> findUsers(String username) {
        return null;
    }

    public UserDTO findUserByAccoundId(String accountId){
        Optional<User> optionalUser = userRepository.findByAccountId(accountId);
        // TODO : DTO로 변환 -> User에? UserDTO에?
        return null;
    }

    public UserDTO updateUser(){
        return null;
    }


}
