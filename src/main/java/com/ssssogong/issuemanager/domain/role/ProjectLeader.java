package com.ssssogong.issuemanager.domain.role;

import com.ssssogong.issuemanager.domain.role.authority.Assignable;
import com.ssssogong.issuemanager.domain.role.authority.Closable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("projectleader")
@PrimaryKeyJoinColumn(name = "pl_id")
public class ProjectLeader extends Role implements Assignable, Closable {
}
