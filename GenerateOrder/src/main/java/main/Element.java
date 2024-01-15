package main;

public class Element {
    private String name;
    private String type;
    private int number;
    private double xLeft;
    private double xRight;
    private double yBottom;
    private double yTop;

    private int maskNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getxLeft() {
        return xLeft;
    }

    public void setxLeft(double xLeft) {
        this.xLeft = xLeft;
    }

    public double getxRight() {
        return xRight;
    }

    public void setxRight(double xRight) {
        this.xRight = xRight;
    }

    public double getyBottom() {
        return yBottom;
    }

    public void setyBottom(double yBottom) {
        this.yBottom = yBottom;
    }

    public double getyTop() {
        return yTop;
    }

    public void setyTop(double yTop) {
        this.yTop = yTop;
    }

    public int getMaskNumber() {
        return maskNumber;
    }

    public void setMaskNumber(int maskNumber) {
        this.maskNumber = maskNumber;
    }
}
