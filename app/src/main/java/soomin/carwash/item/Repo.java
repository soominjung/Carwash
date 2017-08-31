package soomin.carwash.item;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public class Repo {

    Main main;
    Wind wind;

    public Main getMain() {
        return main;
    }


    public Wind getWind() {
        return wind;
    }
}


