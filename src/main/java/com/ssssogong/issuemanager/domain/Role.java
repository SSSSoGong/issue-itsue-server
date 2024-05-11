package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.util.StringCollectionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Convert(converter = StringCollectionConverter.class) // Collection이지만 String 으로 변환하여 하나의 컬럼에 저장
    @Enumerated(EnumType.STRING) // enum 이름 그대로 저장
    @Column
    @Getter
    protected Collection<Privilege> allowedPrivileges;
}
