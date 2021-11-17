package ru.skillbox;

public class Processor {
    private final int frequency;
    private final int numberOfThreads;
    private final String manufacturer;
    private final int weight;

    public int getFrequency() {
        return frequency;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getWeight() {
        return weight;
    }

    public Processor(int frequency, int numberOfThreads, String manufacturer, int weight) {
        this.frequency = frequency;
        this.numberOfThreads = numberOfThreads;
        this.manufacturer = manufacturer;
        this.weight = weight;
    }

    public String toString() {
        return " Информация о процессоре:\n" +
                "    Частота: " + getFrequency() + " МГц\n" +
                "    Количество ядер: " + getNumberOfThreads() + "\n" +
                "    Производитель: " + getManufacturer() + "\n" +
                "    Вес: " + getWeight() + " г\n";
    }
}
