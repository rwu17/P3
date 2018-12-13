package dk.aau.cs.ds308e18.function.tourgen;

import com.graphhopper.util.shapes.GHPoint;
import dk.aau.cs.ds308e18.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class GPSTest {
    private GPS gps = new GPS();


    @Test
    public void setRouteTest() {
        GHPoint ghPointStart = new GHPoint(57.0467, 9.9114); //Coordinates from OpenStreetMap
        GHPoint ghPointEnd = new GHPoint(57.0120, 9.9930);

        Assertions.assertEquals(8500, gps.getDistance(ghPointStart, ghPointEnd), 200); //Expected distance from OpenStreetMap, 200 m delta
        Assertions.assertEquals(780000, gps.getMillis(ghPointStart, ghPointEnd), 300000); //Expected time from OpenStreetMap, 5 min delta
    }

    @Test
    public void setRouteTest2() {

        GHPoint ghPointStart = new GHPoint(56.4490, 9.3658); // Coordinates from OpenStreetMap
        GHPoint ghPointEnd = new GHPoint(55.4719, 9.4813);

        Assertions.assertEquals(120000, gps.getDistance(ghPointStart, ghPointEnd), 200); //Expected distance from OpenStreetMap, 200 m delta
        Assertions.assertEquals(5760000, gps.getMillis(ghPointStart, ghPointEnd), 300000); //Expected time from OpenStreetMap, 5 min delta
    }

    @Test
    public void geocodeAddressTest() {
        GHPoint ghPoint = new GHPoint(57.012281, 9.991705); //Coordinates from OpenStreetMap
        Order order = new Order();
        order.setAddress("selma lagerløfs vej 300");
        order.setZipCode(9220);
        order.requestLatLonFromAddress();

        Assertions.assertEquals(ghPoint.getLat(), order.getLatLon().getLat());
        Assertions.assertEquals(ghPoint.getLon(), order.getLatLon().getLon());

        Order orderBadAddress = new Order();
        orderBadAddress.setAddress("Herluf Trollesvej 3Port 1-8");
        orderBadAddress.requestLatLonFromAddress();
        GHPoint ghPoint00 = new GHPoint(0, 0);

        Assertions.assertEquals(ghPoint00.getLat(), orderBadAddress.getLatLon().lat);
        Assertions.assertEquals(ghPoint00.getLon(), orderBadAddress.getLatLon().lon);
    }

    public ArrayList<Integer> milliConverter(double ms) {

        ArrayList<Integer> timeArray = new ArrayList<>();

        double all = ms;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        // millis -> timer

        all = all / 1000 / 60 / 60;

        // calculates hours on given millis

        if (all >= 1) {
            while (all > 1) {
                all--;
                hours += 1;

            }
        }

        all = all * 60;

        // calculates minutes on given millis

        if (all >= 1) {
            while (all > 1) {
                all--;
                minutes += 1;

            }
        }

        // calculates seconds on given millis

        all = all * 60;

        if (all >= 1) {
            while (all > 1) {
                all--;
                seconds += 1;

            }
        }

        timeArray.add(hours);
        timeArray.add(minutes);
        timeArray.add(seconds);

        return timeArray;
    }

    @Test
    public void milliConverterTest() {
        ArrayList<Integer> test1;
        test1 = milliConverter(4000000);

        if (test1.get(0) != 1) {
            fail("Failed converting hours");

        }

        if (test1.get(1) != 6) {
            fail("Failed converting minutes");

        }

        if (test1.get(2) > 45 || test1.get(2) < 35) {
            fail("Failed converting hours");

        }

    }

}
