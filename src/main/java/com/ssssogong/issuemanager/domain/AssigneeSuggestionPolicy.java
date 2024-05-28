package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import java.util.List;

public interface AssigneeSuggestionPolicy {

    /**
     * Assignee 추천 정책이 다양하게(ex. 랜덤, 가중치.. 등등) 있을 수 있음을 고려하여 인터페이스로 만들었습니다.
     */
    List<User> suggest(Issue issue);
}
