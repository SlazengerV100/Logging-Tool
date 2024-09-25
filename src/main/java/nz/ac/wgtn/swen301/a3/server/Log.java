package nz.ac.wgtn.swen301.a3.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Log {
    public enum Level {
        ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF;

        public static Level toLevel(String levelString) {
            return switch (levelString) {
                case "ALL" -> ALL;
                case "DEBUG" -> DEBUG;
                case "INFO" -> INFO;
                case "WARN" -> WARN;
                case "ERROR" -> ERROR;
                case "FATAL" -> FATAL;
                case "TRACE" -> TRACE;
                case "OFF" -> OFF;
                default -> null;
            };
        }

        public boolean isGreaterOrEqual(Level other) {
            return this.ordinal() >= other.ordinal();
        }
    }

    private final UUID id;
    private final String message;
    private final LocalDateTime timestamp;
    private final String thread;
    private final String logger;
    private final Level level;
    private final String errorDetails;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Log(String message, LocalDateTime timestamp, String thread, String logger, Level level, String errorDetails) {
        id = UUID.randomUUID();
        this.message = message;
        this.timestamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.level = level;
        this.errorDetails = errorDetails;
    }

    public String getId() {
        return id.toString();
    }

    public String getLogger() {
        return logger;
    }

    public Level getLevelAsObject() {
        return level;
    }

    public String getLevel() {
        return level.toString().toLowerCase();
    }

    public String getTimestamp() {
        return formatter.format(timestamp);
    }

    public String getThread() {
        return thread;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
