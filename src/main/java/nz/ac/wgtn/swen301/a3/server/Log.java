package nz.ac.wgtn.swen301.a3.server;

import org.apache.log4j.spi.LoggingEvent;

import java.util.UUID;

public record Log(UUID id, LoggingEvent loggingEvent, String errorDetails) {}
