package com.ssssogong.issuemanager.domain.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**UserDetails 인터페이스 구현 <br>
 * (UserDetails : 사용자 인증 관련 정보를 확인하는 인터페이스)*/
public class CustomUserDetails implements UserDetails {
    private final User user;
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**User의 권한 반환*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 프로젝트마다 권한 달라지는 거 구현 방법
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccountId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
