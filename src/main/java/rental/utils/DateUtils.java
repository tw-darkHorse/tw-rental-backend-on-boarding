package rental.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class DateUtils {
    public static long toTimestamp(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return 0;
        }
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
