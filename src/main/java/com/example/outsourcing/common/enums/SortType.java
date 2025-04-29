package com.example.outsourcing.common.enums;

public enum SortType {
    
    LATEST, // 최신수
    SCORE_DESC, // 별점 높은 순
    SCORE_ASC, // 별점 낮은 순
    WITH_IMAGES; // 이미지 있는 리뷰

    public static SortType from(String value) {
        if (value == null) {
            return LATEST;
        }

        try {
            return SortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LATEST;
        }
    }
}
