package ru.skillbox;

public class Computer {
    private final Processor processor;
    private final Ram ram;
    private final StorageInformation storageInformation;
    private final Screen screen;
    private final Keyboard keyboard;
    private final String vendor;
    private final String name;

    public Computer(String vendor, String name) {
        this.vendor = vendor;
        this.name = name;
        this.ram = new Ram(RamType.DDR4, 0, 0);
        this.processor = new Processor(0,0,"NoManufacturer",0);
        this.storageInformation = new StorageInformation(StorageInformationType.HDD, 0,0);
        this.screen = new Screen(0,ScreenType.VA,0);
        this.keyboard = new Keyboard(KeyboardType.SCISSOR, false,0);
    }

    public Computer(Processor processor, Ram ram, StorageInformation storageInformation, Screen screen, Keyboard keyboard, String vendor, String name) {
        this.processor = processor;
        this.ram = ram;
        this.storageInformation = storageInformation;
        this.screen = screen;
        this.keyboard = keyboard;
        this.vendor = vendor;
        this.name = name;
    }

    public Processor getProcessor() {
        return processor;
    }

    public Ram getRam() {
        return ram;
    }

    public StorageInformation getInformationStorage() {
        return storageInformation;
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

    public Computer setProcessor(Processor processor) {
       return new Computer(processor, ram, storageInformation, screen, keyboard, vendor, name);
    }

    public Computer setRam(Ram ram) {
        return new Computer(processor, ram, storageInformation, screen, keyboard, vendor, name);
    }

    public Computer setStorageInformation(StorageInformation storageInformation) {
        return new Computer(processor, ram, storageInformation, screen, keyboard, vendor, name);
    }

    public Computer setScreen(Screen screen) {
        return new Computer(processor, ram, storageInformation, screen, keyboard, vendor, name);
    }

    public Computer setKeyboard(Keyboard keyboard) {
        return new Computer(processor, ram, storageInformation, screen, keyboard, vendor, name);
    }

    public int calculateWeight() {
        return processor.getWeight() + ram.getWeight() +
                storageInformation.getWeight() + screen.getWeight() +
                keyboard.getWeight();
    }

    public String toString() {
        return "Информация о компьютере:\n" +
                " Вендор: " + getVendor() + "\n" +
                " Имя: " + getName() + "\n" +
                  processor.toString() +
                  ram.toString() +
                  storageInformation.toString() +
                  screen.toString() +
                  keyboard.toString() + "\n" +
                "Общий вес компьютера: " + calculateWeight() + " г\n";
    }
}
