package com.star.influx;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class InfluxUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDB.class);

    private static InfluxDB connection = InfluxDBFactory.connect("http://58.87.84.167:8086", "root", "root");

    static {
        String dbName = "boss";
        connection.createDatabase(dbName);
        connection.setDatabase(dbName);

        String rpName = "bossRetentionPolicy";
        connection.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
        connection.setRetentionPolicy(rpName);

        //connection.enableBatch(100, 10, TimeUnit.SECONDS);
        connection.enableBatch(BatchOptions.DEFAULTS.exceptionHandler((failedPoints, throwable) -> System.out.println(throwable.getMessage())));
    }

    public static InfluxDB getConnection() {
        return connection;
    }

    public static void write(Point point) {
        try {
            getConnection().write(point);
        } catch (Exception e) {
            LOGGER.error("influx write error", e);
        }
    }
}
