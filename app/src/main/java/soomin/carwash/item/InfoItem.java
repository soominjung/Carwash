package soomin.carwash.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Soomin Jung on 2017-09-18.
 */

public class InfoItem {
    public String name;
    @SerializedName("user_distance_meter") public double userDistanceMeter;
    public double lat;
    public double lon;
    //public double washType;
}
