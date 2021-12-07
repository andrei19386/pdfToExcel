public abstract class Client {

    private double amount = 0.0;

    protected double getAmount() {
        System.out.println("На Вашем счету " + amount + "р.");
        return amount;
    }

    protected void put(double amountToPut) {
        if(Double.compare(amountToPut,0.0) < 0) {
            System.out.println("Вы пытаетесь положить отрицательную сумму");
        } else {
            amount += amountToPut;
            System.out.println("Операция пополнения на сумму " + amountToPut + "р. выполнена успешно");
        }
        getAmount();
    }

    protected void take(double amountToTake) {
        if(Double.compare(amount,amountToTake) < 0) {
            System.out.println("Недостаточно средств на счете!");
        } else {
            amount -= amountToTake;
            System.out.println("Операция снятия денег на сумму " + amountToTake + "р. выполнена успешно");
        }
        getAmount();
    }

    public double getCommission(double amount, double commission) {
        return commission * amount / 100;
    }
}
