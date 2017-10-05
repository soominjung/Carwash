package soomin.carwash.lib;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.VisibleRegion;

import soomin.carwash.item.GeoItem;

//위치 정보와 관련된 라이브러리
public class GeoLib {
    private volatile static GeoLib instance;

    public static GeoLib getInstance() {
        if (instance == null) {
            synchronized (GeoLib.class) {
                if (instance == null) {
                    instance = new GeoLib();
                }
            }
        }
        return instance;
    }

    //최근 측정된 위치 정보
    public void setLastKnownLocation(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        int result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if (location != null) {
            GeoItem.knownLatitude = location.getLatitude();
            GeoItem.knownLongitude = location.getLongitude();
        } else {
            //서울 설정
            GeoItem.knownLatitude = 37.566229;
            GeoItem.knownLongitude = 126.977689;
        }
    }

    //화면 중앙으로부터 화면 왼쪽까지의 거리 반환
    public int getDistanceMeterFromScreenCenter(GoogleMap map) {
        VisibleRegion vr = map.getProjection().getVisibleRegion();
        double left = vr.latLngBounds.southwest.longitude;

        Location leftLocation = new Location("left");
        leftLocation.setLatitude(vr.latLngBounds.getCenter().latitude);
        leftLocation.setLongitude(left);

        Location center=new Location("center");
        center.setLatitude( vr.latLngBounds.getCenter().latitude);
        center.setLongitude( vr.latLngBounds.getCenter().longitude);
        return  (int) center.distanceTo(leftLocation);
    }
}
