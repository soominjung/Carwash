package soomin.carwash.item;

/**
 * Created by Soomin Jung on 2017-09-17.
 */

public class rainReport {
    int noRain;
    int afterRain;

    public rainReport(int afterRain, int noRain){
        this.afterRain=afterRain;
        this.noRain=noRain;
    }



    public void setAfterRain(int afterRain) {
        this.afterRain = afterRain;
    }
    public void setNoRain(int noRain) {
        this.noRain = noRain;
    }

    public int getAfterRain() {
        return afterRain;
    }
    public int getNoRain() {
        return noRain;
    }
}
