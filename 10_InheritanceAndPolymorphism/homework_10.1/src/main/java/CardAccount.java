public class CardAccount extends BankAccount {
    public static final int COMMISSION_PERCENT = 1;

    public CardAccount() {
        super();
    }


    public CardAccount(double amount) {
        super(amount);
    }

    protected boolean take(double amountToTake) {
        double amountToTakeWithCommission = amountToTake + (double) COMMISSION_PERCENT / 100 * amountToTake;
        return super.take(amountToTakeWithCommission);
    }
}
