package nz.ac.wgtn.swen301.a3.server;

import org.apache.log4j.spi.LoggingEvent;

import java.util.Arrays;
import java.util.UUID;

public class Log {
    private final LoggingEvent loggingEvent;
    private final UUID id;

    public Log(LoggingEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
        id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

    public LoggingEvent getLoggingEvent() {
        return loggingEvent;
    }

    public String getErrorDetails() {
        return Arrays.toString(loggingEvent.getThrowableInformation().getThrowable().getStackTrace());
    }
}
