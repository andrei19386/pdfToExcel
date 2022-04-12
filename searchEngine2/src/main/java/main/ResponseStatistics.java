package main;

public class ResponseStatistics {
    private boolean result;
    private StatisticsInfo statistics;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public StatisticsInfo getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsInfo statistics) {
        this.statistics = statistics;
    }
}
