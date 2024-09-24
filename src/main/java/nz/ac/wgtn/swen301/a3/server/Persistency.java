package nz.ac.wgtn.swen301.a3.server;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

public class Persistency extends AppenderSkeleton {
    public static List<Log> DB = new ArrayList<>();

    public static void reset() {
        DB = new ArrayList<>();
    }

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
