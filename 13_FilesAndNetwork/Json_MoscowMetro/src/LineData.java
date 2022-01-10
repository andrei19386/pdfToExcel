public class LineData {
    private final String dataNumber;//Идентификатор линии
    private final String name;//Название линии

    public LineData(String dataNumber, String name) {
        this.dataNumber = dataNumber;
        this.name = name;
    }
    public String getDataNumber() {
        return dataNumber;
    }
    public String getName() {
        return name;
    }
}