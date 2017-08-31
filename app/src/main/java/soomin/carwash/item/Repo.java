package soomin.carwash.item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//import soomin.carwash.item.Item;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public class Repo {
    /*City city;
    public class City{
        String country;

        public String getCountry() {
            return country;
        }
    }

    public City getCity() {
        return city;
    }*/

    List<Item> list;

    public List<Item> getList(){
        return list;
    }

    public class Item {

        temp temp;

        public class temp {
            double day;

            public double getDay() {
                return day;
            }
        }

        public temp getTemp() {
            return temp;
        }
    }


    /*
    Main main;
    Wind wind;

    public Main getMain() {
        return main;
    }


    public Wind getWind() {
        return wind;
    }
    */
}


