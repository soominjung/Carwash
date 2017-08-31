package soomin.carwash.item;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public interface ApiInterface {
    @GET("/data/2.5/weather")
    Call<Repo> repo(@Query("appid") String appid, @Query("lat") double lat, @Query("lon") double lon);
}