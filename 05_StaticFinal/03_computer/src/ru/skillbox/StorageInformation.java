package ru.skillbox;

public class StorageInformation {
    private final StorageInformationType storageInformationType;
    private final int memoryVolume;
    private final int weight;

    public StorageInformation(StorageInformationType storageInformationType, int memoryVolume, int weight) {
        this.storageInformationType = storageInformationType;
        this.memoryVolume = memoryVolume;
        this.weight = weight;
    }

    public StorageInformationType getStorageInformationType() {
        return storageInformationType;
    }

    public int getMemoryVolume() {
        return memoryVolume;
    }

    public int getWeight() {
        return weight;
    }

    public String toString(){
        return  " Информация о накопителе информации: \n" +
                "    Тип носителя: " + getStorageInformationType() + "\n" +
                "    Объем памяти: " + getMemoryVolume() + " Гбайт\n" +
                "    Вес: " + getWeight() + " г\n";
    }
}
