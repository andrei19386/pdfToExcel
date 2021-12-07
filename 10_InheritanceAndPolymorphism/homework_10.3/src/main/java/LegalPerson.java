public class LegalPerson extends Client {
    public static final double COMMISSION = 1; //%

    @Override
    protected void take(double amountToTake) {
        System.out.println("Снятие денег с комиссией " + COMMISSION + " %" );
        double withCommission = Double.sum(amountToTake, getCommission(amountToTake,COMMISSION));
        super.take(withCommission);
    }
}
