package com.star.influx;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfluxApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testWrite() {

        InfluxDB connection = InfluxUtils.getConnection();
        long timeMillis = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Point point = Point.measurement("boss_exception")
                    .time(System.currentTimeMillis() + i, TimeUnit.MILLISECONDS)
                    .tag("serverName", "message-center")
                    .tag("companyCode", "100")
                    .tag("userName", "fkx")
                    .tag("exceptionType", "RuntimeException")
                    .tag("exceptionCode", "unknown")
                    .addField("elapsedTime", i)
                    .build();
            connection.write(point);

            Point.Builder builder = Point.measurement("boss_exception")
                    .time(System.currentTimeMillis() + i, TimeUnit.MILLISECONDS);

            if ("mess" != null){
                builder.tag("serName", "value")
            }

        }
        System.out.println(System.currentTimeMillis() - timeMillis);
        try {
            Thread.sleep(1000L * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQuery() {
        Query boss = new Query("select * from boss_exception", "boss");
        QueryResult query = InfluxUtils.getConnection().query(boss);
        System.out.println(query);
    }
}
