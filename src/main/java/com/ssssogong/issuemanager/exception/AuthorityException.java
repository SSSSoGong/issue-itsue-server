package com.ssssogong.issuemanager.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorityException extends RuntimeException{
    public AuthorityException(String message){
        super(message);
    }
}
