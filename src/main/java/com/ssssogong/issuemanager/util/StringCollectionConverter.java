package com.ssssogong.issuemanager.util;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * String 컬렉션을 String으로 변환
 * 엔티티 속성 DB에 저장할 때 converter로 활용
 * ex) ["ISSUE_REPORTABLE", "ISSUE_FIXABLE"] => "ISSUE_REPORTABLE;ISSUE_FIXABLE"
 */
public class StringCollectionConverter implements AttributeConverter<Collection<String>, String> {
    private static final String SPLIT_CHAR = ";";  // String 구분자

    @Override
    /** List<String> 타입의 엔티티 속성을 String 타입 DB 칼럼으로 변환 */
    public String convertToDatabaseColumn(Collection<String> strings) {
        return strings != null ? String.join(SPLIT_CHAR, strings.toArray(String[]::new)) : "";
    }


    @Override
    /** String 타입 DB 칼럼을 List<String> 타입 엔티티 속성으로 변환 */
    public List<String> convertToEntityAttribute(String string) {
        return string != null ? Arrays.asList(string.split(SPLIT_CHAR)) : emptyList();
    }
}
