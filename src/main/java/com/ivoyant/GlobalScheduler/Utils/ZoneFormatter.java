package com.ivoyant.GlobalScheduler.Utils;


import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class ZoneFormatter {


    public Timestamp formatToZone(Date date, String zoneId) {
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedDateTime = currentTimestamp.toInstant().atZone(zone);
        OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();
        return Timestamp.valueOf(offsetDateTime.toLocalDateTime());
    }
}
