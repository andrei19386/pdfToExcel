import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
//import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Airport airport = Airport.getInstance();
        List<Flight>  flights = findPlanesLeavingInTheNextTwoHours(airport);
        flights.forEach(System.out::println);
    }

    public static List<Flight> findPlanesLeavingInTheNextTwoHours(Airport airport) {

        return  getAllFlights(airport).stream()
                .filter(Main::dateAndTypeFilter)
                //.sorted(Comparator.comparing(Flight::getDate))
                .collect(Collectors.toList());
    }

    private static List<Flight> getAllFlights(Airport airport) {

        List<Flight> flightsAll = new ArrayList<>();
        airport.getTerminals()
                .forEach( t -> flightsAll.addAll( t.getFlights() ) );

        return flightsAll;
    }

    private static boolean dateAndTypeFilter(Flight flight) {

        LocalDateTime dateTimeFrom = LocalDateTime.now();
        LocalDateTime dateTimeUntil = dateTimeFrom.plusHours(2);

        LocalDateTime dateTimeOfFlight = flight.getDate()
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return flight.getType() == Flight.Type.DEPARTURE &&
                dateTimeOfFlight.isBefore(dateTimeUntil) &&
                dateTimeOfFlight.isAfter(dateTimeFrom);
    }
}