import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XMLHandler extends DefaultHandler {
    Voter voter;

    public static  final int MAX_WORKTIMES = 300;
    private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private Map<Voter, Integer> voterCounts;
    private Map<Integer, WorkTime> voteStationWorkTimes;

    public XMLHandler() {
        voterCounts = new HashMap<>();
        voteStationWorkTimes =  new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter") && voter == null) {
                Date birthDay = birthDayFormat.parse(attributes.getValue("birthDay"));
                voter = new Voter(attributes.getValue("name"), birthDay);
            }
            else if(qName.equals("visit") && voter != null) {
                int count = voterCounts.getOrDefault(voter,0);
                voterCounts.put(voter,count + 1);

                int station = Integer.parseInt(attributes.getValue("station"));
                Date time = visitDateFormat.parse(attributes.getValue("time"));

                WorkTime workTime = voteStationWorkTimes.get(station);
                if (workTime == null) {
                    workTime = new WorkTime();
                    voteStationWorkTimes.put(station, workTime);
                }
                workTime.addVisitTime(time.getTime());

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
       if(qName.equals("voter")) {
           voter = null;
       }
    }

    public void printDuplicatedVoters() {
        for(Voter voter : voterCounts.keySet()) {
            int count = voterCounts.get(voter);
            if(count > 1){
                System.out.println(voter.toString() + " - " + count);
            }
        }
    }


    public void printTimes() {
        for (int votingStation : voteStationWorkTimes.keySet()) {
            WorkTime workTime = voteStationWorkTimes.get(votingStation);
            System.out.println("\t" + votingStation + " - " + workTime);
        }
    }

}
