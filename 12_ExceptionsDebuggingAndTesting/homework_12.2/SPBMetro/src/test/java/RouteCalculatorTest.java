import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteCalculatorTest extends TestCase {

    List<Station> route;
    RouteCalculator routeCalculator;
    StationIndex stationIndex;

    Station station1st;
    Station station2nd;
    Station station3d;
    Station station4th;
    Station station5th;
    Station station6th;

    @Override
    protected void setUp() throws Exception {

      route = new ArrayList<>();
      stationIndex = new StationIndex();

      Line line1  = new Line(1,"Первая");
      Line line2  = new Line(2, "Вторая");
      Line line3  = new Line(3, "Третья");

      stationIndex.addLine(line1);
      stationIndex.addLine(line2);
      stationIndex.addLine(line3);

      station1st = new Station("Чернышевская", line1);
      station2nd = new Station("Площадь восстания", line1);
      station3d = new Station("Маяковская", line3);
      station4th = new Station("Гостиный двор", line3);
      station5th = new Station("Невский проспект", line2);
      station6th = new Station("Сенная площадь", line2);

        line1.addStation(station1st);
        line1.addStation(station2nd);
        line3.addStation(station3d);
        line3.addStation(station4th);
        line2.addStation(station5th);
        line2.addStation(station6th);

        stationIndex.addStation(station1st);
        stationIndex.addStation(station2nd);
        stationIndex.addStation(station3d);
        stationIndex.addStation(station4th);
        stationIndex.addStation(station5th);
        stationIndex.addStation(station6th);

        stationIndex.addConnection(Arrays.asList(station2nd,station3d));
        stationIndex.addConnection(Arrays.asList(station4th,station5th));

        routeCalculator = new RouteCalculator(stationIndex);

        route.add(station1st);
        route.add(station2nd);
        route.add(station3d);
        route.add(station4th);
        route.add(station5th);
        route.add(station6th);
    }

    public void testCalculateDuration(){
       double actual = RouteCalculator.calculateDuration(route);
       double expected = 14.5;
       assertEquals(expected,actual,0.01);
    }

    public void testGetRouteOnTheLine(){
        List<Station> actual = routeCalculator.getShortestRoute(station1st,station2nd);
        List<Station> expected = new ArrayList<>();
        expected.add(station1st);
        expected.add(station2nd);
        assertEquals(expected,actual);
    }

    public void testGetRouteWithOneConnection(){
        List<Station> actual = routeCalculator.getShortestRoute(station1st,station4th);
        List<Station> expected = new ArrayList<>();
        expected.add(station1st);
        expected.add(station2nd);
        expected.add(station3d);
        expected.add(station4th);
        assertEquals(expected,actual);
    }


    public void testGetRouteWithTwoConnections(){
        List<Station> actual = routeCalculator.getShortestRoute(station1st,station6th);
        List<Station> expected = new ArrayList<>();
        expected.add(station1st);
        expected.add(station2nd);
        expected.add(station3d);
        expected.add(station4th);
        expected.add(station5th);
        expected.add(station6th);
        assertEquals(expected,actual);
    }

    public void testGetRouteViaConnectedLine(){
        List<Station> actual = routeCalculator.getShortestRoute(station2nd,station5th);
        List<Station> expected = new ArrayList<>();
        expected.add(station2nd);
        expected.add(station3d);
        expected.add(station4th);
        expected.add(station5th);
        assertEquals(expected,actual);
    }

    public void testIsConnected(){
        List<Station> actual = routeCalculator.getShortestRoute(station2nd,station3d);
        List<Station> expected = new ArrayList<>();
        expected.add(station2nd);
        expected.add(station3d);
        assertEquals(expected,actual);
    }

    public void testGetShortestRoute(){

        Line line4  = new Line(4, "Новая");

        stationIndex.addLine(line4);

        Station station7th = new Station("Достоевская", line4);
        Station station8th = new Station("Лужайка", line4);

        line4.addStation(station7th);
        line4.addStation(station8th);

        stationIndex.addStation(station7th);
        stationIndex.addStation(station8th);

        stationIndex.addConnection(Arrays.asList(station1st,station7th));
        stationIndex.addConnection(Arrays.asList(station6th,station8th));

        RouteCalculator routeCalculatorTest = new RouteCalculator(stationIndex);

        List<Station> actual = routeCalculatorTest.getShortestRoute(station1st,station8th);
        List<Station> expected = new ArrayList<>();
        expected.add(station1st);
        expected.add(station7th);
        expected.add(station8th);

        assertEquals(expected,actual);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
