import java.time.LocalDate;

public class DepositAccount extends BankAccount {

    private LocalDate lastIncome;

    public DepositAccount() {
        super();
        lastIncome = LocalDate.now();
    }

    public DepositAccount(double amount) {
        super(amount);
        lastIncome = LocalDate.now();
    }

    protected void put(double amountToPut) {
        super.put(amountToPut);
        lastIncome = LocalDate.now();
        // lastIncome = LocalDate.of(2021,11,7);
    }


    protected boolean take(double amountToTake) {
        boolean passedMonthFromLastIncome = LocalDate.now().minusMonths(1).compareTo(lastIncome) >= 0 ? true : false;
        if(!passedMonthFromLastIncome){
            System.out.println("C момента предыдущего пополнения не прошел месяц. Вы не можете снять деньги");
            return false;
        }
        super.take(amountToTake);
        return true;
    }
}
