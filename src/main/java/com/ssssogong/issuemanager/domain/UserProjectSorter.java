package com.ssssogong.issuemanager.domain;

import java.util.Comparator;
import java.util.List;

public class UserProjectSorter {

    /**
     * 프로젝트를 최신순으로 정렬하되, 아직 접근하지 않은(accessTime이 null)인 것은.. 일단 위쪽에..?
     * [즐겨찾기 한 프로젝트들, 접근하지 않은 프로젝트들, 나머지 프로젝트 최신순 정렬]
     */
    public static List<UserProject> sortByAccessTimeAndIsFavorite(final List<UserProject> userProjects) {
        userProjects.sort(Comparator.comparing(UserProject::isFavorite).reversed()
                .thenComparing(up -> up.getAccessTime() == null ? 0 : 1)
                .thenComparing(UserProject::getAccessTime, Comparator.nullsFirst(Comparator.reverseOrder())));
        return userProjects;
    }
}
