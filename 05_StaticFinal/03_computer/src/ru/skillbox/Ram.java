package ru.skillbox;

public class Ram {
    private final RamType ramType;
    private final int volume;
    private final int weight;

    public RamType getRamType() {
        return ramType;
    }

    public int getVolume() {
        return volume;
    }

    public int getWeight() {
        return weight;
    }

    public Ram(RamType ramType, int volume, int weight) {
        this.ramType = ramType;
        this.volume = volume;
        this.weight = weight;
    }

    public String toString(){
        return " Информация об оперативной памяти:\n" +
               "    Тип оперативной памяти: " + getRamType() + "\n" +
               "    Объем оперативной памяти: " + getVolume() + " Гбайт\n" +
               "    Вес: " + getWeight() + " г\n";
    }
}
