package com.miniBili.utils;


import com.miniBili.entity.enums.DateTimePatternEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {

    private static final Object lockObj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String getBeforeDay(Integer Day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-Day);
        return format(calendar.getTime(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
    }

    public  static List<String>getBeforeDays(Integer beforeDays){
        LocalDate  endDate = LocalDate.now();
        List<String>dateList = new ArrayList<>();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i=beforeDays;i>0;i--){
            dateList.add(endDate.minusDays(i).format(formater));
        }
        return dateList;
    }
}
