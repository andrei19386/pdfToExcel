package ru.skillbox;

public class Main {

    public static void main(String[] args) {
        //Создание объектов-комплектующих при помощи конструкторов
        ProcessorClass processor = new ProcessorClass(4300, 4, "Intel", 2);
        Ram ram = new Ram(RamType.DDR3, 4, 1);
        InformationStorage informationStorage = new InformationStorage(InformationStorageType.SDD, 128, 1);
        Screen screen = new Screen(23.8,ScreenType.IPS,4050);
        Keyboard keyboard = new Keyboard(KeyboardType.MEMBRANE, true, 1020);

        //Создание объекта-компьютера с помощью конструктора и последующее укахание комплектующих при помощи сеттеров
        Computer computer = new Computer("HP","MyComputer");
        computer.setProcessor(processor);
        computer.setRam(ram);
        computer.setInformationStorage(informationStorage);
        computer.setScreen(screen);
        computer.setKeyboard(keyboard);
        //Вывод информации о компьютере
        System.out.println(computer.toString());
    }
}
