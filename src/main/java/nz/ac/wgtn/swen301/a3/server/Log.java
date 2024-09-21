package nz.ac.wgtn.swen301.a3.server;

import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class Log {
    private final LoggingEvent loggingEvent;
    private final UUID id;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public Log(LoggingEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
        id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return loggingEvent.getLoggerName();
    }

    public String getLevel() {
        return loggingEvent.getLevel().toString().toLowerCase();
    }

    public String getTimestamp() {
        return formatter.format(new Date(loggingEvent.getTimeStamp()));
    }

    public String getThread() {
        return loggingEvent.getThreadName();
    }

    public String getMessage() {
        return loggingEvent.getRenderedMessage();
    }

    public LoggingEvent getLoggingEvent() {
        return loggingEvent;
    }

    public String getErrorDetails() {
        if (loggingEvent.getThrowableInformation() != null) {
            return Arrays.toString(loggingEvent.getThrowableInformation().getThrowable().getStackTrace());
        }
        return null;
    }
}
