package com.hammatov.MySecondTestAppSpringBoot.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
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

    // Сколько миллисекунд прошло от момента systemTime до момента now.
    // systemTime должен быть в формате ISO-8601, например 2023-10-07T07:47:56.503Z.
    // Если строку не удалось разобрать, выбрасывается DateTimeParseException.
    public static long millisSince(String systemTime, Instant now) {
        Instant sent = OffsetDateTime.parse(systemTime).toInstant();
        return Duration.between(sent, now).toMillis();
    }

}
