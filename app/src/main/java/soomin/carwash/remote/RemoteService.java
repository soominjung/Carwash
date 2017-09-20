package soomin.carwash.remote;

/**
 * Created by Soomin Jung on 2017-09-18.
 */

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import soomin.carwash.item.InfoItem;


public interface RemoteService {
    String BASE_URL = "http://211.253.25.114:80/";

    @GET("/cw/map/list")
    Call<ArrayList<InfoItem>> listMap(@Query("lat") double lat,
                                      @Query("lon") double lon,
                                      @Query("distance") int distance,
                                      @Query("user_lat") double userLat,
                                      @Query("user_lon") double userLon);
}