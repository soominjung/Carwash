package soomin.carwash;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soomin.carwash.item.CWInfoItem;
import soomin.carwash.item.GeoItem;
import soomin.carwash.item.InfoItem;
import soomin.carwash.lib.GeoLib;
import soomin.carwash.remote.RemoteService;
import soomin.carwash.remote.ServiceGenerator;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                     GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    GoogleMap map;

    LatLng currentLatLng;
    int distanceMeter = 640;
    int currentZoomLevel = 10;
    boolean isOnList = false;
    boolean isMarkerClicked = false;
    boolean firstMarkerClick = false;

    Toast zoomGuideToast;

    FragmentManager fm = getFragmentManager();
    FragmentTransaction fragmentTransaction;
    CWInfoFragment cwinfo;

    private HashMap<Marker, InfoItem> markerMap = new HashMap<>();

    ArrayList<InfoItem> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("내 주변 세차장 찾기");
        toolbar.setBackgroundColor(Color.rgb(16,163,178));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setInfoWindowAdapter(null);

        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, fineLocationPermission)
                != PackageManager.PERMISSION_GRANTED) return;

        map.setMyLocationEnabled(true);

        map.setOnCameraMoveListener(this);
        map.setOnCameraIdleListener(this);

        UiSettings setting = map.getUiSettings();
        setting.setMyLocationButtonEnabled(true);
        setting.setCompassEnabled(true);
        setting.setZoomControlsEnabled(true);
        setting.setMapToolbarEnabled(false);
        map.setMinZoomPreference(11);

        if (GeoItem.getKnownLocation() != null) {
            movePosition(GeoItem.getKnownLocation(), 14);
        }
        showList();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String[] str = marker.getSnippet().split("\\|");

        CWInfoItem cwinfoItem;

        if(str.length==1) {
            cwinfoItem = new CWInfoItem(marker.getPosition().latitude, marker.getPosition().longitude,
                    marker.getTitle(), Double.parseDouble(str[0]), " ");
        }
        else {
            cwinfoItem = new CWInfoItem(marker.getPosition().latitude, marker.getPosition().longitude,
                    marker.getTitle(), Double.parseDouble(str[0]), str[1]);
        }

        if (!isFinishing() && !isDestroyed())
        {
            CachePot.getInstance().push(cwinfoItem);
            cwinfo = new CWInfoFragment();
            fragmentTransaction = fm.beginTransaction();
            if (firstMarkerClick) {
                fragmentTransaction.add(R.id.fm2, cwinfo).commit();
                firstMarkerClick=false;
            }else{
                fragmentTransaction.replace(R.id.fm2, cwinfo).commit();
            }
            isMarkerClicked = true;
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(isMarkerClicked){
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.remove(cwinfo).commit();
            isMarkerClicked=false;
        }
    }

    private void movePosition(LatLng latlng, float zoomLevel) {
        CameraPosition cp = new CameraPosition.Builder().target((latlng)).zoom(zoomLevel).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

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
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    setMap(list);
                    infoList = list;
                } else {
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InfoItem>> call, Throwable t) {
            }
        });
    }

    private void setMap(ArrayList<InfoItem> list) {
        if (map != null && list != null) {
            map.clear();
            addMarker(list);
        }
    }

    private void addMarker(ArrayList<InfoItem> list) {

        if (list == null || list.size() == 0) return;

        for (InfoItem item : list) {
            if (item.lat != 0 && item.lon != 0) {
                Marker marker = map.addMarker(getMarker(item));

                markerMap.put(marker, item);
            }
        }
    }

    private MarkerOptions getMarker(InfoItem item) {
        final MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(item.lat, item.lon));
        marker.title(item.name);
        marker.snippet(Double.toString(item.userDistanceMeter)+"|"+item.washType);
        marker.draggable(false);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

        return marker;
    }

    @Override
    public void onCameraMove() {
        //GeoLib.getInstance().setLastKnownLocation(MapActivity.this);
        //showList();
    }

    @Override
    public void onCameraIdle() {
        GeoLib.getInstance().setLastKnownLocation(MapActivity.this);
        showList();
    }

    private void showList() {
        currentZoomLevel = (int) map.getCameraPosition().zoom;
        currentLatLng = map.getCameraPosition().target;

        if (currentZoomLevel < 12) {

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