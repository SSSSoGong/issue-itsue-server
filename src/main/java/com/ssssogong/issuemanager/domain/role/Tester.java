package com.ssssogong.issuemanager.domain.role;

import com.ssssogong.issuemanager.domain.role.authority.IssueReportable;
import com.ssssogong.issuemanager.domain.role.authority.Reopenable;
import com.ssssogong.issuemanager.domain.role.authority.Resolvable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("tester")
@PrimaryKeyJoinColumn(name = "tester_id")
public class Tester extends Role implements Resolvable, IssueReportable, Reopenable {
}
