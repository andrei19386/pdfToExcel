public class Arithmetic {
    private int variable1 = 0; //Первая переменная
    private int variable2 = 0; //Вторая переменная

    public int sum() {
        return variable1 + variable2;
    } //Сумма двух чисел

    public int multiplication() {
        return variable1 * variable2;
    } //Произведение двух чисел

    public int maxValue() { //Максимальное из двух чисел
        if(variable1 > variable2) {
            return variable1;
        }
        else {
            return variable2;
        }
    }

    public int minValue() { //Минимальное из двух чисел
        if(variable1 < variable2) {
            return variable1;
        }
        else {
            return variable2;
        }
    }

    public Arithmetic(int variable1, int variable2) {
        this.variable1 = variable1;
        this.variable2 = variable2;
    }
}
