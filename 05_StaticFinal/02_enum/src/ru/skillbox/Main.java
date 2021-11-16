package ru.skillbox;

public class Main {
    public static void main(String[] args) {
        ArithmeticCalculator arithmeticCalculator = new ArithmeticCalculator(43,11);
        System.out.println("Первое число в конструкторе калькулятора равно " + arithmeticCalculator.getVariableFirst());
        System.out.println("Второе число в конструкторе калькулятора равно " + arithmeticCalculator.getVariableSecond());
        System.out.println("Сумма чисел равна " + arithmeticCalculator.calculate(Operation.ADD));
        System.out.println("Разность чисел равна " + arithmeticCalculator.calculate(Operation.SUBTRACT));
        System.out.println("Произведение чисел равно " + arithmeticCalculator.calculate(Operation.MULTIPLY));
    }
 }
