package soomin.carwash.item;

import com.google.android.gms.maps.model.LatLng;

//위치 정보 저장
public class GeoItem {
    public static double knownLatitude;
    public static double knownLongitude;


     //사용자 위도, 경도 객체 반환
    public static LatLng getKnownLocation() {
        if (knownLatitude == 0 || knownLongitude == 0) {
            return new LatLng(37.566229, 126.977689);
        } else {
            return new LatLng(knownLatitude, knownLongitude);
        }
    }
}