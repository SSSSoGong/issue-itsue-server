package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.dto.UserDTO;
import com.ssssogong.issuemanager.dto.UserResponseDTO;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO register(){
        return null;
    }

    public Object login(){
        return null;
    }

    public UserResponseDTO unregister(){
        return null;
    }

    public Collection<UserDTO> findUsers(){
        return null;
    }

    public UserDTO findUserByAccoundId(){
        return null;
    }

    public UserDTO updateUser(){
        return null;
    }


}
