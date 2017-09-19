package soomin.carwash.item;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Soomin Jung on 2017-09-19.
 */

public class CWInfoItem {
    public String name;
    @SerializedName("user_distance_meter") public double userDistanceMeter;
    public double lat;
    public double lon;
    public String washType;

    public CWInfoItem(double lat, double lon, String name, double userDistanceMeter, String washType){
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.userDistanceMeter = userDistanceMeter;
        this.washType = washType;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getUserDistanceMeter() {
        return userDistanceMeter;
    }

    public String getName() {
        return name;
    }

    public String getWashType() {
        return washType;
    }
}
