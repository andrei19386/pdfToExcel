package ru.skillbox;

public class Screen {
    private final double diagonal;
    private final ScreenType screenType;
    private final int weight;

    public Screen(double diagonal, ScreenType screenType, int weight) {
        this.diagonal = diagonal;
        this.screenType = screenType;
        this.weight = weight;
    }

    public double getDiagonal() {
        return diagonal;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public int getWeight() {
        return weight;
    }

    public String toString(){
        return  " Информация об экране:\n" +
                "    Диагональ: " + getDiagonal() + " \"\n" +
                "    Тип: " + getScreenType() + "\n" +
                "    Вес: " + getWeight() + " г\n";
    }
}
