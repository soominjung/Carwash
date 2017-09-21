package soomin.carwash;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kimkevin.cachepot.CachePot;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;

import soomin.carwash.item.CWInfoItem;


public class CWInfoFragment extends Fragment {

    TextView tvName;
    TextView tvType;
    TextView tvDist;
    ImageView navibtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cwinfo, container, false);
        tvName = (TextView) v.findViewById(R.id.cwName);
        tvType = (TextView) v.findViewById(R.id.cwType);
        tvDist = (TextView) v.findViewById(R.id.tvDist);

        final CWInfoItem cwInfoItem = CachePot.getInstance().pop(CWInfoItem.class);

        tvType.setText(cwInfoItem.getWashType());

        String str = cwInfoItem.getName();
        if (str != null && str.length() > 16) {
            str = str.substring(0, 16) + "..";
        }

        tvName.setText(str);

        String dist;
        double distanced = cwInfoItem.getUserDistanceMeter();
        if(distanced>=1000){
            dist="~"+String.format("%.1f",distanced/1000)+"km ";
        }else
            dist="~"+Integer.toString((int) distanced)+"m ";

        tvDist.setText(dist);

        navibtn = (ImageView) v.findViewById(R.id.ivNavi);
        navibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setMessage("카카오내비를 이용해 길안내를 받겠습니까?");

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NaviFunc(cwInfoItem.getLat(),cwInfoItem.getLon(),cwInfoItem.getName());
                    }

                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }

                });

                alert.show();

                //NaviFunc(cwInfoItem.getLat(),cwInfoItem.getLon(),cwInfoItem.getName());
            }
        });

        return v;
    }

    public void NaviFunc(double lat, double lon, String des){
        KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(Location.newBuilder(des, lon, lat).build()).setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build());
        KakaoNaviService.navigate(getActivity(), builder.build());
    }
}

