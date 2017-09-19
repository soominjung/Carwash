package soomin.carwash;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.kimkevin.cachepot.CachePot;

import soomin.carwash.item.CWInfoItem;


public class CWInfoFragment extends Fragment {

    TextView tvSeq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cwinfo, container, false);
        tvSeq = (TextView) v.findViewById(R.id.cwName);

        CWInfoItem cwInfoItem = CachePot.getInstance().pop(CWInfoItem.class);
        tvSeq.setText(""+cwInfoItem.getUserDistanceMeter());
        return v;
    }
}

