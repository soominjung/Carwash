package soomin.carwash.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import soomin.carwash.R;
import soomin.carwash.item.InfoItem;


/**
 * 구글 지도 맵에서 맛집 정보 리스트의 아이템을 처리하는 어댑터
 */
public class MapListAdapter extends RecyclerView.Adapter<MapListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<InfoItem> itemList;

    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public MapListAdapter(Context context, int resource, ArrayList<InfoItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;
    }

    /**
     * 새로운 아이템 리스트를 설정한다.
     * @param itemList 새로운 아이템 리스트
     */
    public void setItemList(ArrayList<InfoItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 아이템 크기를 반환한다.
     * @return 아이템 크기
     */
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    /**
     * 뷰홀더(ViewHolder)를 생성하기 위해 자동으로 호출된다.
     * @param parent 부모 뷰그룹
     * @param viewType 새로운 뷰의 뷰타입
     * @return 뷰홀더 객체
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(v);
    }

    /**
     * 뷰홀더(ViewHolder)와 아이템을 리스트 위치에 따라 연동한다.
     * @param holder 뷰홀더 객체
     * @param position 리스트 위치
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InfoItem item = itemList.get(position);

        int meter = (int) item.userDistanceMeter;

        if (meter == 0) {
            holder.distanceMeter.setText("");
        } else if (meter < 1000) {
            holder.distanceMeter.setText(meter + "m");
        } else {
            holder.distanceMeter.setText( (meter / 1000)
                    + "km");
        }

        holder.name.setText(item.name);

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goBestFoodInfoActivity(context, item.seq);
            }
        });*/
    }

    public String getSubString(Context context, String str, int max) {
        if (str != null && str.length() > max) {
            return str.substring(0, max) + "다음";
        } else {
            return str;
        }
    }

    /**
     * 이미지를 설정한다.
     * @param imageView  이미지를 설정할 뷰
     * @param fileName 이미지 파일이름
     */
    private void setImage(ImageView imageView, String fileName) {

        if (isBlank(fileName)) {
            Picasso.with(context).load(R.drawable.refresh).into(imageView);
        } else {

        }
    }

    public boolean isBlank(String str) {
        if (str == null || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 아이템을 보여주기 위한 뷰홀더 클래스
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView distanceMeter;
        TextView name;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            distanceMeter = (TextView) itemView.findViewById(R.id.distance_meter);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
