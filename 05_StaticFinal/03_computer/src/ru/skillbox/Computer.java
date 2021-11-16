package ru.skillbox;

public class Computer {
    private ProcessorClass processor;
    private Ram ram;
    private InformationStorage informationStorage;
    private Screen screen;
    private Keyboard keyboard;
    private final String vendor;
    private final String name;

    public Computer(String vendor, String name) {
        this.vendor = vendor;
        this.name = name;
    }

    public ProcessorClass getProcessor() {
        return processor;
    }

    public Ram getRam() {
        return ram;
    }

    public InformationStorage getInformationStorage() {
        return informationStorage;
    }

    public Screen getScreen() {
        return screen;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public void setProcessor(ProcessorClass processor) {
        this.processor = processor;
    }

    public void setRam(Ram ram) {
        this.ram = ram;
    }

    public void setInformationStorage(InformationStorage informationStorage) {
        this.informationStorage = informationStorage;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public int calculateWeight() {
        return processor.getWeight() + ram.getWeight() +
                informationStorage.getWeight() + screen.getWeight() +
                keyboard.getWeight();
    }

    public String toString() {
        return "Информация о компьютере:\n" +
                " Вендор: " + getVendor() + "\n" +
                " Имя: " + getName() + "\n" +
                  processor.toString() +
                  ram.toString() +
                  informationStorage.toString() +
                  screen.toString() +
                  keyboard.toString() + "\n" +
                "Общий вес компьютера: " + calculateWeight() + " г\n";
    }
}
