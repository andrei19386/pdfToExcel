import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XMLHandler extends DefaultHandler {
    Voter voter;

    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    private int limit = 1_500_000;
    private int number = 0;

    private Map<Integer, WorkTime> voteStationWorkTimes;

    public XMLHandler() {
        voteStationWorkTimes =  new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter") && voter == null) {
                Date birthDay = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), birthDay);
            }
            else if(qName.equals("visit") && voter != null && number < limit) {

                DBConnection.countVoter(voter.getName(), birthDayFormat.format(voter.getBirthDay()));
                number++;
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
       if(qName.equals("voter")) {
           voter = null;
       }
    }

    public void printDuplicatedVoters() throws SQLException {
        DBConnection.printVoterCounts();
    }


    /*public void printTimes() {
        for (int votingStation : voteStationWorkTimes.keySet()) {
            WorkTime workTime = voteStationWorkTimes.get(votingStation);
            System.out.println("\t" + votingStation + " - " + workTime);
        }
    }*/

}
