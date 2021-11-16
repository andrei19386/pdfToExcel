package ru.skillbox;

public class InformationStorage {
    private final InformationStorageType informationStorageType;
    private final int memoryVolume;
    private final int weight;

    public InformationStorage(InformationStorageType informationStorageType, int memoryVolume, int weight) {
        this.informationStorageType = informationStorageType;
        this.memoryVolume = memoryVolume;
        this.weight = weight;
    }

    public InformationStorageType getInformationStorageType() {
        return informationStorageType;
    }

    public int getMemoryVolume() {
        return memoryVolume;
    }

    public int getWeight() {
        return weight;
    }

    public String toString(){
        return  " Информация о накопителе информации: \n" +
                "    Тип носителя: " + getInformationStorageType() + "\n" +
                "    Объем памяти: " + getMemoryVolume() + " Гбайт\n" +
                "    Вес: " + getWeight() + " г\n";
    }
}
