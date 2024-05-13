package com.ssssogong.issuemanager.domain.role;

import com.ssssogong.issuemanager.util.StringCollectionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Role implements GrantedAuthoritiesContainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    /**Role에 주어진 권한 목록*/
    @Convert(converter = StringCollectionConverter.class) // Collection => String 으로 변환하여 하나의 컬럼에 저장
    @Enumerated(EnumType.STRING)
    @Column
    @Getter
    protected Collection<Privilege> allowedPrivileges;
}
