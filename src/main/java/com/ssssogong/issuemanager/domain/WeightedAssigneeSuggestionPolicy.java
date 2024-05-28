package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class WeightedAssigneeSuggestionPolicy implements AssigneeSuggestionPolicy {

    /**
     * 이슈에 할당할 Assignee 3명을 추천하는 기능
     * - 유저 별로 가중치를 둬서 가중치가 가장 높은 사람 추천
     *  1. category별로 해결(resolve까지 완료)한 사람
     *  2. 중요도 높은 거 해결한 사람(BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL)
     */

    private static final int MAX_SUGGESTION_COUNT = 3;
    private static final int SAME_CATEGORY_WEIGHT = 5; //같은 카테고리의 이슈를 해결했다면 가중치 준다.
    private static final Map<Priority, Integer> priorityWeights = new EnumMap<>(Priority.class);

    static {
        // 해결한 이슈의 중요도에 따른 가중치를 준다.
        priorityWeights.put(Priority.BLOCKER, 5);
        priorityWeights.put(Priority.CRITICAL, 4);
        priorityWeights.put(Priority.MAJOR, 3);
        priorityWeights.put(Priority.MINOR, 2);
        priorityWeights.put(Priority.TRIVIAL, 1);
    }

    @Override
    public List<User> suggest(final Issue issue, final List<User> developers) {
        final Project project = issue.getProject();
        final List<Issue> issues = project.getIssues();
        final Category category = issue.getCategory();
        System.out.println("category = " + category);

        // 유저별 가중치 초기화
        Map<User, Integer> userWeights = new HashMap<>();
        for (User developer : developers) {
            userWeights.put(developer, 0);
        }

        for (Issue each : issues) {
            if(each.hasResolvedHistory()) {
                final User fixer = each.getFixer();
                //중요도 가중치
                final Priority priority = each.getPriority();
                userWeights.computeIfPresent(fixer, (key, weight) -> weight + priorityWeights.get(priority));
                //카테고리 가중치
                System.out.println("each.getCategory() = " + each.getCategory());
                if(category == each.getCategory()) {
                    System.out.println("들어왔다!: " + fixer.getAccountId());
                    userWeights.computeIfPresent(fixer, (key, weight) -> weight + SAME_CATEGORY_WEIGHT);
                }
            }
        }

        for (User user : userWeights.keySet()) {
            System.out.println("user = " + user.getAccountId());
            System.out.println("userWeights.get(user) = " + userWeights.get(user));
        }

        // 최대 MAX_SUGGESTION_COUNT 만큼의 유저 리스트를 반환
        return userWeights.entrySet().stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .limit(MAX_SUGGESTION_COUNT)
                .map(Map.Entry::getKey)
                .toList();
    }
}
