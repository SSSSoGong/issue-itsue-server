package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProjectFavoriteRequest {

    private boolean isFavorite;
}
