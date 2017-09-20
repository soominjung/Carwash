package soomin.carwash.item;

import java.util.List;
//import soomin.carwash.item.Item;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public class Repo {

    List<Item> list;

    public List<Item> getList(){
        return list;
    }

    public class Item {

        List<Item2> weather;
        public List<Item2> getList2() { return weather; }

        public class Item2{
            int id;

            public int getId(){
                return id;
            }
        }
    }
}


