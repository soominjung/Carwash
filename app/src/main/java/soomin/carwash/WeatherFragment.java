package soomin.carwash;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.kimkevin.cachepot.CachePot;

import java.util.List;

import soomin.carwash.item.ImageViewList;
import soomin.carwash.item.Repo;


public class WeatherFragment extends Fragment {

    ImageView iv;
    private Repo repo;
    private ImageViewList ivList = new ImageViewList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        //iv = (ImageView) v.findViewById(R.id.imageView);

        repo = CachePot.getInstance().pop(Repo.class);

        List<Integer> ivIDList=ivList.getIvList();

        for(int i=0;i<repo.getList().size();i++) {
            iv = (ImageView) v.findViewById(ivIDList.get(i));
            if(repo.getList().get(i).getList2().get(0).getId()==500)
                iv.setImageResource(R.drawable.w09d);
        }

        //if(repo.getList().get(0).getList2().get(0).getId()==800)
        //    iv.setImageResource(R.drawable.w01d);

        return v;
    }

}
