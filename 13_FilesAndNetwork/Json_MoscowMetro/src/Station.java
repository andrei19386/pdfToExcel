public class Station  {
    private final String name;//Название станции
    private String lineId;//Идентификатор линии
    private int stationIndex;//Уникальный идентификатор станции

    public int getStationIndex() {
        return stationIndex;
    }

    public void setStationIndex(int stationIndex) {
        this.stationIndex = stationIndex;
    }

    public Station(String name) {
        this.name = name;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getName() {
        return name;
    }

    public String getLineId() {
        return lineId;
    }

}