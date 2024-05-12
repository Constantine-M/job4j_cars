package ru.job4j.cars.util;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Constantine on 10.05.2024
 */
public class TimeZoneUtil {

    public static List<TimeZone> getAllAvailableTimeZones() {
        var timeZonesIds = TimeZone.getAvailableIDs();
        return Arrays.stream(timeZonesIds)
                .map(TimeZone::getTimeZone)
                .toList();
    }

    /**
     * Данный метод преобразует дату и время создания
     * задачи согласно часовому поясу пользователя.
     *
     * Храним в БД в "UTC", а выводим для
     * пользователя согласно его часовому поясу!
     *
     * Если часовой пояс не выбран, то время создания
     * задачи отображается по умолчанию в UTC Time zone.
     *
     * @param user залогиненый пользователь
     * @param posts список объявлений о продаже машин
     */
    public static void setDateTimeByUserTimezone(User user, Collection<Post> posts) {
        if (user.getUserZone().isEmpty()) {
            user.setUserZone("UTC");
        } else {
            posts.forEach(post -> post.setCreated(post.getCreated()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.of(user.getUserZone()))
                    .toLocalDateTime()));
        }
    }
}
