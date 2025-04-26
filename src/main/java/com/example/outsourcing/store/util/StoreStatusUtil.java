package com.example.outsourcing.store.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StoreStatusUtil {


    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean isOpen(String openingTimes, String closingTimes) {
        LocalTime now = LocalTime.now();
        LocalTime openingTime = LocalTime.parse(openingTimes, TIME_FORMATTER);
        LocalTime closingTime = LocalTime.parse(closingTimes, TIME_FORMATTER);

        // 오픈시간 ~ 마감시간 사이에 있으면 "영업 중"
        if (openingTime.isBefore(closingTime)) {
            return !now.isBefore(openingTime) && !now.isAfter(closingTime);
        } else {
            // 24시 넘어가는 가게 (예: 22:00 ~ 04:00) 케이스 처리
            return !now.isBefore(openingTime) || !now.isAfter(closingTime);
        }
    }
}
