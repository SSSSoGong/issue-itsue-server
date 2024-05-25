package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProjectFavoriteRequestDto {

    private boolean isFavorite;
}
