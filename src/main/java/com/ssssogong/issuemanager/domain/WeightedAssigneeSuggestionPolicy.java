package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
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
    private final WeightCalculator categoryWeightCalculator = (currentIssue, pastIssue) -> {
        int SAME_CATEGORY_WEIGHT = 5; //같은 카테고리의 이슈를 해결했다면 가중치 준다.
        return currentIssue.getCategory() == pastIssue.getCategory() ? SAME_CATEGORY_WEIGHT : 0;
    };
    private final WeightCalculator priorityWeightCalculator = (currentIssue, pastIssue) -> {
        // 우선순위에 따른 가중치를 준다.
        Map<Priority, Integer> priorityWeights = new EnumMap<>(Priority.class);
        priorityWeights.put(Priority.BLOCKER, 5);
        priorityWeights.put(Priority.CRITICAL, 4);
        priorityWeights.put(Priority.MAJOR, 3);
        priorityWeights.put(Priority.MINOR, 2);
        priorityWeights.put(Priority.TRIVIAL, 1);

        return priorityWeights.getOrDefault(pastIssue.getPriority(), 0);
    };

    @Override
    public List<User> suggest(final Issue issue, final List<User> developers) {
        final Project project = issue.getProject();
        final List<Issue> issues = project.getIssues();
        Map<User, Integer> userWeights = initializeWeights(developers);

        for (Issue each : issues) {
            if (each.hasResolvedHistory()) {
                final User fixer = each.getFixer();
                int categoryWeight = categoryWeightCalculator.calculateWeight(issue, each);
                int priorityWeight = priorityWeightCalculator.calculateWeight(issue, each);
                userWeights.computeIfPresent(fixer, (key, weight) -> weight + priorityWeight + categoryWeight);
            }
        }

        return findTopWeightedUsers(userWeights);
    }

    private Map<User, Integer> initializeWeights(final List<User> developers) {
        Map<User, Integer> userWeights = new HashMap<>();
        for (User developer : developers) {
            userWeights.put(developer, 0);
        }
        return userWeights;
    }

    private List<User> findTopWeightedUsers(final Map<User, Integer> userWeights) {
        return userWeights.entrySet().stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .limit(MAX_SUGGESTION_COUNT)
                .map(Map.Entry::getKey)
                .toList();
    }
}
