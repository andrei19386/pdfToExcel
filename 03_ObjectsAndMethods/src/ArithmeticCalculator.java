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
        if(operation == Operation.ADD ) {
            return variableFirst + variableSecond;
        }
        if(operation == Operation.SUBTRACT ) {
            return variableFirst - variableSecond;
        }
        return  variableFirst * variableSecond;
    }
}
