package nz.ac.wgtn.swen301.a3.server;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.util.List;

public class Persistency extends AppenderSkeleton {
    public static List<Log> DB;

    @Override
    protected void append(LoggingEvent loggingEvent) {
        DB.add(new Log(loggingEvent));
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
