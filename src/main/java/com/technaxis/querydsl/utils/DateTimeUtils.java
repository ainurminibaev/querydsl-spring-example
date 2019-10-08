package com.technaxis.querydsl.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author Dmitry Sadchikov
 */
public final class DateTimeUtils {

    public static final LocalDate EPOCH_DATE = LocalDate.ofEpochDay(0);
    public static final DateTimeFormatter FILES_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm");

    public static boolean isIntersectTwoTimeIntervals(Time from1, Time to1, Time from2, Time to2) {
        return from1.getTime() < to2.getTime() && from2.getTime() < to1.getTime();
    }

    public static boolean isIntersectTwoTimeIntervals(LocalTime from1, LocalTime to1, LocalTime from2, LocalTime to2) {
        return from1.isBefore(to2) && from2.isBefore(to1);
    }

    public static boolean isBetweenOrEquals(LocalTime time, LocalTime from, LocalTime to) {
        return (time.isAfter(from) || time.equals(from)) && (time.isBefore(to) || time.equals(to));
    }

    public static boolean isBetweenOrEquals(Time time, Time from, Time to) {
        return isBetweenOrEquals(time.toLocalTime(), from.toLocalTime(), to.toLocalTime());
    }

    public static boolean isBetweenOrEquals(LocalDate date, LocalDate from, LocalDate to) {
        return (date.isAfter(from) || date.equals(from)) && (date.isBefore(to) || date.equals(to));
    }

    public static boolean isBetweenOrEquals(Date date, Date from, Date to) {
        return isBetweenOrEquals(date.toLocalDate(), from.toLocalDate(), to.toLocalDate());
    }

    public static boolean isBetweenOrEquals(LocalDateTime dateTime, LocalDateTime from, LocalDateTime to) {
        return (dateTime.isAfter(from) || dateTime.equals(from)) && (dateTime.isBefore(to) || dateTime.equals(to));
    }

    public static boolean isBetweenOrEquals(Timestamp dateTime, Timestamp from, Timestamp to) {
        return isBetweenOrEquals(dateTime.toLocalDateTime(), from.toLocalDateTime(), to.toLocalDateTime());
    }

    public static List<Long> splitByHourInMillis(Time from, Time to) {
        return splitByHoursInMillis(from, to, 1);
    }

    public static List<Long> splitByHoursInMillis(Time from, Time to, long hours) {
        return splitByHours(from, to, hours, DateTimeUtils::getTimeInMillis);
    }

    public static List<Time> splitByHour(Time from, Time to) {
        return splitByHours(from, to, 1);
    }

    public static List<Time> splitByHours(Time from, Time to, long hours) {
        return splitByHours(from, to, hours, Time::valueOf);
    }

    private static <T> List<T> splitByHours(Time from, Time to, long hours,
                                            Function<LocalTime, T> functionFromLocalTime) {
        if (from == null || to == null) {
            return Collections.emptyList();
        }
        List<T> splits = new ArrayList<>();
        LocalTime localTimeFrom = roundDownMinutes(from);
        LocalTime localTimeTo = roundUpMinutes(to);
        splits.add(functionFromLocalTime.apply(localTimeFrom));
        LocalTime split = localTimeFrom.plusHours(hours);
        while (split.isBefore(localTimeTo)) {
            splits.add(functionFromLocalTime.apply(split));
            split = split.plusHours(hours);
        }
        splits.add(functionFromLocalTime.apply(split));
        return splits;
    }

    public static List<Date> splitByDay(Date start, Date end) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        List<Date> splits = new ArrayList<>();
        LocalDate from = start.toLocalDate();
        LocalDate to = end.toLocalDate();
        splits.add(Date.valueOf(from));
        if (from.isEqual(to)) {
            return splits;
        }
        LocalDate split = from.plusDays(1);
        while (split.isBefore(to)) {
            splits.add(Date.valueOf(split));
            split = split.plusDays(1);
        }
        splits.add(Date.valueOf(to));
        return splits;
    }

    public static LocalDateTime localDateTimeFrom(Date date, Time time) {
        if (date == null || time == null) {
            return null;
        }
        return LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
    }


    public static LocalDateTime localDateTimeFrom(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public static Timestamp timestampFrom(LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }

    public static Timestamp timestampFrom(Date date, Time time) {
        if (date == null || time == null) {
            return null;
        }
        return Timestamp.valueOf(localDateTimeFrom(date, time));
    }

    public static LocalTime roundDownMinutes(Time time) {
        return time.toLocalTime().withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalTime roundUpMinutes(Time time) {
        LocalTime localTime = time.toLocalTime().withSecond(0).withNano(0);
        if (localTime.getMinute() != 0) {
            localTime = localTime.plusMinutes(60 - localTime.getMinute());
        }
        return localTime;
    }

    public static LocalTime utcLocalTimeRelativeToZoneOffset(LocalTime localTime, ZoneOffset zoneOffset) {
        return OffsetTime.of(localTime, zoneOffset).withOffsetSameInstant(ZoneOffset.UTC).toLocalTime();
    }

    public static Long getCurrentTimeInMillis() {
        return getTimeInMillis(LocalTime.now());
    }

    public static Long getTimeInMillis(Time time) {
        if (time == null) {
            return null;
        }
        return getTimeInMillis(time.toLocalTime());
    }

    public static Long getTimeInMillis(LocalTime time) {
        if (time == null) {
            return null;
        }
        LocalTime midnight = utcLocalTimeRelativeToZoneOffset(LocalTime.MIDNIGHT, AppConstants.MOSCOW_ZONE_OFFSET);
        LocalDate date = time.isBefore(midnight) ? EPOCH_DATE.plusDays(1) : EPOCH_DATE;
        return Timestamp.valueOf(LocalDateTime.of(date, time)).getTime();
    }
}
