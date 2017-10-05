package soomin.carwash;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kimkevin.cachepot.CachePot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import soomin.carwash.item.DayList;
import soomin.carwash.item.ImageViewList;
import soomin.carwash.item.Repo;


public class WeatherFragment extends Fragment {

    ImageView iv;
    TextView tv;
    private Repo repo;
    private ImageViewList ivList = new ImageViewList();
    private DayList dayList = new DayList();
    DateFormat df = new SimpleDateFormat("MM/dd");
    Calendar cal = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        String strDate;
        List<Integer> dayIDList= dayList.getDayList();
        for(int j=0;j<12;j++){
            tv=(TextView) v.findViewById(dayIDList.get(j));
            strDate=addDate(j+2);
            tv.setText(strDate);
        }

        repo = CachePot.getInstance().pop(Repo.class);

        if(repo!=null) {
            List<Integer> ivIDList=ivList.getIvList();
            for (int i = 0; i < repo.getList().size()&&i<14; i++) {
                iv = (ImageView) v.findViewById(ivIDList.get(i));

                switch(repo.getList().get(i).getList2().get(0).getId()/100){
                    case 2:
                        iv.setImageResource(R.drawable.w11d);
                        break;
                    case 3:
                    case 5:
                        iv.setImageResource(R.drawable.w09d);
                        break;
                    case 6:
                        iv.setImageResource(R.drawable.w13d);
                        break;
                    case 7:
                        iv.setImageResource(R.drawable.w50d);
                        break;
                    case 8:
                        switch(repo.getList().get(i).getList2().get(0).getId()) {
                            case 800:
                                iv.setImageResource(R.drawable.w01d);
                                break;
                            case 801:
                                iv.setImageResource(R.drawable.w02d);
                                break;
                            case 802:
                                iv.setImageResource(R.drawable.w03d);
                                break;
                            case 803:
                            case 804:
                                iv.setImageResource(R.drawable.w04d);
                                break;
                        }
                        break;
                }
            }
        }

        return v;
    }

    public String addDate(int i){
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH,i);

        String strDate = df.format(cal.getTime());
        return strDate;
    }

}
