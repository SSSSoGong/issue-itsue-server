package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WeightedAssigneeSuggestionPolicy implements AssigneeSuggestionPolicy {

    /**
     * 이슈에 할당할 Assignee 3명을 추천하는 기능
     * - 유저 별로 가중치를 둬서 가중치가 가장 높은 사람 추천
     *  1. category별로 해결(resolve까지 완료)한 사람
     *  2. 중요도 높은 거 해결한 사람(BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL)
     */

    @Override
    public List<User> suggest(final Issue issue) {
        return null;
    }
}
