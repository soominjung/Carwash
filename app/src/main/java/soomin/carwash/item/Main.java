package soomin.carwash.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public class Main {
    Double temp;
    Integer pressure;
    Integer humidity;
    Double temp_min;
    Double temp_max;

    public Double getTemp() {
        return temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getTemp_min() {
        return temp_min;
    }

    public Double getTemp_max() {
        return temp_max;
    }
}

