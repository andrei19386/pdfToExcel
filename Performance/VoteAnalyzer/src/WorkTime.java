import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

public class WorkTime {

    public static final int MAX_PERIODS = 3;
    public static final int FROM_TO = 2;


    // private TreeSet<TimePeriod> periods;
    private long[][] periodsAsLong;

    /**
     * Set of TimePeriod objects
     */

    public WorkTime() {

       // periods = new TreeSet<>();
        periodsAsLong = new long[MAX_PERIODS][FROM_TO];
    }

    public void addVisitTime(long visitTime) throws ParseException {
        Date visit = new Date(visitTime);
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date visitDate = dayFormat.parse(dayFormat.format(new Date(visitTime)));
        for (int i = 0; i < MAX_PERIODS; i++)
        {
            Date  currentDate = dayFormat.parse(dayFormat.format(new Date(periodsAsLong[i][0])));
            if (visitDate.compareTo(currentDate) == 0) {
                if (visitTime < periodsAsLong[i][0]) {
                    periodsAsLong[i][0] = visitTime;
                }
                if (visitTime > periodsAsLong[i][1]) {
                    periodsAsLong[i][1] = visitTime;
                }
                return;
            }
            if (periodsAsLong[i][0] == 0) {
                periodsAsLong[i][0] = visitTime;
                periodsAsLong[i][1] = visitTime;
                break;
            }
        }


       // periods.add(new TimePeriod(visitTime, visitTime));
    }

    public String toString() {
        String line = "";
        for (int i = 0; i < MAX_PERIODS; i++)
            {
                if (!line.isEmpty()) {
                    line += ", ";
                }
                TimePeriod period = new TimePeriod(periodsAsLong[i][0],periodsAsLong[i][1]);
                line += period;
            }

      /*  for (TimePeriod period : periods) {
            if (!line.isEmpty()) {
                line += ", ";
            }
            line += period;
        }*/
        return line;
    }
}
