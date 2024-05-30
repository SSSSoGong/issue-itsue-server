package com.ssssogong.issuemanager.domain;

@FunctionalInterface
interface WeightCalculator {
    int calculateWeight(Issue currentIssue, Issue pastIssue);
}
