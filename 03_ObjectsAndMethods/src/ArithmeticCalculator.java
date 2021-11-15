public class ArithmeticCalculator {
    private final int variable_first;
    private final int variable_second;

    public ArithmeticCalculator(int variable_first, int variable_second) {
        this.variable_first = variable_first;
        this.variable_second = variable_second;
    }

    public int getVariable_first() {
        return variable_first;
    }

    public int getVariable_second() {
        return variable_second;
    }

    public double calculate(Operation operation) {
        if(operation == Operation.ADD ) {
            return variable_first + variable_second;
        }
        if(operation == Operation.SUBTRACT ) {
            return variable_first - variable_second;
        }
        if(operation == Operation.MULTIPLY ) {
            return variable_first * variable_second;
        }
        return  0;
    }
}
