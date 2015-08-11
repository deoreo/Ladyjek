package ladyjek.twiscode.com.ladyjek.Model;

import java.io.Serializable;

/**
 * Created by ModelUser on 8/11/2015.
 */
public class ModelHistory implements Serializable {
    private String id, date, time, driver, from, destination, distance, duration, price;

    public ModelHistory(String id, String date, String time, String driver,
                     String from, String destination, String distance, String duration,
                     String price
    ){
        this.id = id;
        this.date = date;
        this.time = time;
        this.driver = driver;
        this.from = from;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
