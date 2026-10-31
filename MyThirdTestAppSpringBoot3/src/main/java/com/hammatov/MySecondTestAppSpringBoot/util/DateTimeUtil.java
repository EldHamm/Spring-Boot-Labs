package com.hammatov.MySecondTestAppSpringBoot.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    // SimpleDateFormat не потокобезопасен, поэтому каждый вызов возвращает новый объект.
    // Буква Z в конце шаблона записана как текст, поэтому время приводится к UTC.
    public static SimpleDateFormat getCustomFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}
