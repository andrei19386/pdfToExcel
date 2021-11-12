package ru.skillbox;

public class Elevator {
    private int currentFloor;
    private int minFloor;
    private int maxFloor;

    public Elevator(int minFloor, int maxFloor){ //Конструктор класса лифт
        this.currentFloor = 1;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        System.out.println("Вы находитесь на 1 этаже");
    }
    public int getCurrentFloor(){
        return currentFloor;
    }

    private void moveDown(){
        currentFloor = currentFloor > minFloor ? currentFloor - 1 : currentFloor;
    }

    private void moveUp(){
        currentFloor = currentFloor < maxFloor ? currentFloor + 1 : currentFloor;
    }

    public void move(int floor){
        if(floor > maxFloor || floor < minFloor){
            System.out.println("Этаж задан некорректно! Вы не можете подняться выше " + maxFloor + " этажа и опуститься ниже " + minFloor + " этажа");
            return;
        }

        while(currentFloor < floor){
            moveUp();
            System.out.println("Текущий этаж после перемещения - " + getCurrentFloor() );
        }
        while(currentFloor > floor){
            moveDown();
            System.out.println("Текущий этаж после перемещения - " + getCurrentFloor() );
        }
    }

}
