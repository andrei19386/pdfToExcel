package ru.skillbox;

public class ArithmeticCalculator {
    private final int variableFirst;
    private final int variableSecond;

    public ArithmeticCalculator(int variableFirst, int variableSecond) {
        this.variableFirst = variableFirst;
        this.variableSecond = variableSecond;
    }

    public int getVariableFirst() {
        return variableFirst;
    }

    public int getVariableSecond() {
        return variableSecond;
    }

    public int calculate(Operation operation) {
        int result;
        switch(operation){
            case ADD -> result = variableFirst + variableSecond;
            case MULTIPLY -> result = variableFirst * variableSecond;
            case SUBTRACT ->  result = variableFirst - variableSecond;
            default -> result = 0;
        }
        return result;
    }
}
