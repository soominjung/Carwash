package soomin.carwash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soomin.carwash.adapter.MapListAdapter;
import soomin.carwash.item.GeoItem;
import soomin.carwash.item.InfoItem;
import soomin.carwash.lib.GeoLib;
import soomin.carwash.remote.RemoteService;
import soomin.carwash.remote.ServiceGenerator;

/**
 * 지도에서 맛집 위치를 보여주는 프래그먼트
 */
public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                     /*GoogleMap.OnMapClickListener,*/ GoogleMap.OnCameraMoveListener {
    private final String TAG = this.getClass().getSimpleName();

    Context context;

    int memberSeq;
    GoogleMap map;

    LatLng currentLatLng;
    int distanceMeter = 640;
    int currentZoomLevel = 10;
    boolean isOnList = false;

    Toast zoomGuideToast;

    private HashMap<Marker, InfoItem> markerMap = new HashMap<>();

    RecyclerView list;
    MapListAdapter adapter;
    ArrayList<InfoItem> infoList = new ArrayList<>();

    Button listOpen;

    /**
     * MapFragment 인스턴스를 생성해서 반환한다.
     * @return MapFragment 인스턴스

    public static MapFragment newInstance() {
        MapFragment f = new MapFragment();
        return f;
    }*/
    /**
     * activity_map.xml 기반으로 뷰를 생성한다.
     * @param inflater XML를 객체로 변환하는 LayoutInflater 객체
     * @param container null이 아니라면 부모 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     * @return 생성한 뷰 객체

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        View v = inflater.inflate(R.layout.activity_map, container, false);

        return v;
    }*/

    /**
     * onCreateView() 메소드 뒤에 호출되며 구글맵을 화면에 보여준다.
     * 그리고 화면 구성을 위한 작업을 한다.
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu2);
        setContentView(R.layout.activity_map);
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);

        adapter = new MapListAdapter(context, R.layout.row_map, infoList);
    }

    /**
     * 구글맵이 준비되었을 때 호출되며 구글맵을 설정하고 기본 마커를 추가하는 작업을 한다.
     * @param map 구글맵 객체
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setInfoWindowAdapter(null);

        map.setOnMarkerClickListener(this);

        String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, fineLocationPermission)
                != PackageManager.PERMISSION_GRANTED) return;

        map.setMyLocationEnabled(true);

        map.setOnCameraMoveListener(this);

        UiSettings setting = map.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(false);

        Toast.makeText(MapActivity.this,GeoItem.getKnownLocation().toString(),Toast.LENGTH_LONG).show();

        if (GeoItem.getKnownLocation() != null) {
            movePosition(GeoItem.getKnownLocation(), 10);
        }
        showList();
    }
    /**
     * 구글맵에서 마커가 클릭되었을 때 호출된다.
     * @param marker 클릭한 마커에 대한 정보를 가진 객체
     * @return 마커 이벤트를 처리했다면 true, 그렇지 않다면 false
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        InfoItem item = markerMap.get(marker);
        //GoLib.getInstance().goBestFoodInfoActivity(context, item.seq);
        return true;
    }

    /**
     * 구글맵의 카메라를 위도와 경도 그리고 줌레벨을 기반으로 이동한다.
     * @param latlng 위도, 경도 객체
     * @param zoomLevel 줌레벨
     */
    private void movePosition(LatLng latlng, float zoomLevel) {
        CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(zoomLevel).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    /**
     * 주어진 정보를 기반으로 맛집 정보를 조회하고 지도에 표시한다.
     * @param latLng 위도, 경도 객체
     * @param distance 거리
     * @param userLatLng 사용자 현재 위도, 경도 객체
     */
    private void listMap(LatLng latLng, int distance, LatLng userLatLng) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<InfoItem>> call = remoteService.listMap(latLng.latitude,
                latLng.longitude, distance, userLatLng.latitude, userLatLng.longitude);
        call.enqueue(new Callback<ArrayList<InfoItem>>() {
            @Override
            public void onResponse(Call<ArrayList<InfoItem>> call,
                                   Response<ArrayList<InfoItem>> response) {
                ArrayList<InfoItem> list = response.body();

                if (list == null) {
                    Toast.makeText(MapActivity.this,"list is null",Toast.LENGTH_LONG).show();
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    Toast.makeText(MapActivity.this,"before setMap",Toast.LENGTH_LONG).show();
                    setMap(list);
                    infoList = list;
                } else {
                    Toast.makeText(MapActivity.this,"not success",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InfoItem>> call, Throwable t) {
            }
        });
    }

    /**
     * 맛집 리스트를 지도에 표시하는 메소드를 호출하고 지도에서 원을 그린다.
     * @param list 맛집 리스트
     */
    private void setMap(ArrayList<InfoItem> list) {
        if (map != null && list != null) {
            map.clear();
            addMarker(list);
        }
        adapter.setItemList(list);
        drawCircle(currentLatLng);
    }

    /**
     * 맛집 리스트를 지도에 추가한다.
     * @param list 맛집 리스트
     */
    private void addMarker(ArrayList<InfoItem> list) {
        Toast.makeText(MapActivity.this,"addMarker list.size()"+list.size(),Toast.LENGTH_LONG).show();

        if (list == null || list.size() == 0) return;

        for (InfoItem item : list) {
            Toast.makeText(MapActivity.this,"addMarker"+item,Toast.LENGTH_LONG).show();
            if (item.lat != 0 && item.lon != 0) {
                Marker marker = map.addMarker(getMarker(item));

                markerMap.put(marker, item);
            }
        }
    }

    /**
     * FoodInfoItem으로 지도에 표시할 마커를 생성한다.
     * @param item 맛집 정보 아이템 객체
     * @return 지도에 표시할 마커 객체
     */
    private MarkerOptions getMarker(InfoItem item) {
        final MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(item.lat, item.lon));
        marker.title(item.name);
        //marker.snippet(item.tel);
        marker.draggable(false);

        return marker;
    }
    private void drawCircle(LatLng position) {
        double radiusInMeters = distanceMeter;
        int strokeColor = 0x440000ff;
        int shadeColor = 0x110000ff;

        CircleOptions circleOptions
                = new CircleOptions().center(position).radius(radiusInMeters)
                .fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(4);
        map.addCircle(circleOptions);
    }
    /**
     * 지도를 움직일 경우 맛집 정보를 조회해서 화면에 표시할 수 있도록 한다.
     */
    @Override
    public void onCameraMove() {
        showList();
    }

    /**
     * 지도를 일정 레벨 이상 확대했을 경우, 해당 위치에 있는 맛집 리스트를 서버에 요청한다.
     */
    private void showList() {
        currentZoomLevel = (int) map.getCameraPosition().zoom;
        currentLatLng = map.getCameraPosition().target;

        if (currentZoomLevel < 10) {

            map.clear();

            if (zoomGuideToast != null) {
                zoomGuideToast.cancel();
            }
            zoomGuideToast = Toast.makeText(MapActivity.this
                    , "지도를 좀 더 확대해야 배너가 보입니다."
                    , Toast.LENGTH_SHORT);
            zoomGuideToast.show();

            return;
        }

        distanceMeter = GeoLib.getInstance().getDistanceMeterFromScreenCenter(map);

        listMap(currentLatLng, distanceMeter, GeoItem.getKnownLocation());
    }
}