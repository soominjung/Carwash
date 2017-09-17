package soomin.carwash.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soomin Jung on 2017-09-17.
 */

public class CityPositionList {

    List<Position> cpList=new ArrayList<Position>();

    public class Position{
        double lat;
        double lon;

        public Position(double lat, double lon){
            this.lat=lat;
            this.lon=lon;
        }

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public List<Position> getCpList() {

        cpList.add(new Position(0,0));//현위치
        cpList.add(new Position(37.5683,126.9778));//서울
        cpList.add(new Position(35.1028,129.0403));//부산
        cpList.add(new Position(35.8703,128.5911));//대구
        cpList.add(new Position(37.4536,126.7317));//인천
        cpList.add(new Position(35.1667,126.9167));//광주
        cpList.add(new Position(36.3214,127.4197));//대전
        cpList.add(new Position(35.5372,129.3167));//울산
        cpList.add(new Position(37.2444,127.0089));//경기남부
        cpList.add(new Position(37.6564,126.835));//경기북부
        cpList.add(new Position(37.8747,127.7342));//강원
        cpList.add(new Position(36.6372,127.4897));//충청북도
        cpList.add(new Position(36.8065,127.1522));//충청남도
        cpList.add(new Position(35.8219,127.1489));//전라북도
        cpList.add(new Position(34.7936,126.3886));//전라남도
        cpList.add(new Position(36.0322,129.365));//경상북도
        cpList.add(new Position(35.2281,128.6811));//경상남도
        cpList.add(new Position(33.5097,126.5219));//제주

        return cpList;
    }
}