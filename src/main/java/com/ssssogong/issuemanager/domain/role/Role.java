package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorColumn(name = "name") // name을 dtype 칼럼으로 설정
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Role implements GrantedAuthoritiesContainer {
    /**
     * Role에 주어진 권한 목록
     */
/*    @Convert(converter = StringCollectionConverter.class) // Collection => String 으로 변환하여 하나의 컬럼에 저장
    @Enumerated(EnumType.STRING)
    @Column
    */
    @Getter
    @Transient
    protected Collection<Privilege> allowedPrivileges;
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    // TODO: enum Role을 새로 만들고, 이 클래스는 RolePrivilege로 개명해서 Role과 Privilege를 매핑하는 엔티티로 바꿀까?
    @Column(insertable = false, updatable = false)
    @Getter
    private String name;

    /**
     * 자신의 역할과 Privilege를 담은 Collection을 반환한다
     */
    @Override
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!name.isBlank())
            authorities.add(new SimpleGrantedAuthority("ROLE_" + name.toUpperCase()));

        authorities.addAll(
                allowedPrivileges
                        .stream()
                        .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                        .toList()
        );
        return authorities;
    }

    public boolean isRole(String roleName) {
        return this.getClass().getSimpleName().equalsIgnoreCase(roleName);
    }

    public String getRoleName() {
        return this.getClass().getSimpleName();
    }
}
