package com.ssssogong.issuemanager.domain.role;

import com.ssssogong.issuemanager.domain.role.authority.Fixable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("developer")
@PrimaryKeyJoinColumn(name = "dev_id")
public class Developer extends Role implements Fixable {
}
