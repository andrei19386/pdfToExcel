package ru.skillbox;

public class Keyboard {
    private final KeyboardType keyboardType;
    private final boolean hasBlackLight;
    private final int weight;

    public Keyboard(KeyboardType keyboardType, boolean hasBlackLight, int weight) {
        this.keyboardType = keyboardType;
        this.hasBlackLight = hasBlackLight;
        this.weight = weight;
    }

    public KeyboardType getKeyboardType() {
        return keyboardType;
    }

    public boolean getHasBlackLight() {
        return hasBlackLight;
    }

    public int getWeight() {
        return weight;
    }

    public String toString(){
        String keyboardType;
        switch(getKeyboardType()) {
            case MEMBRANE -> keyboardType = "Мембранная";
            case SCISSOR -> keyboardType = "Ножничная";
            default -> keyboardType = "Неизвестная";
        }

        return " Информация о клавиатуре:\n" +
               "    Тип: " + keyboardType  + "\n" +
               "    Наличие подсветки: " + (getHasBlackLight() ? "Да" : "Нет") + "\n" +
               "    Вес: " + getWeight() + " г\n";
    }
}
