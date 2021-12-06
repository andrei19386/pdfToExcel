public class Main {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount(0.0);
        bankAccount.put(-100);
        bankAccount.put(100);
        bankAccount.take(200);
        bankAccount.take(99);

        System.out.println("Осталось денег на счету - " + bankAccount.getAmount() + "р.");

        CardAccount cardAccount = new CardAccount(0.0);
        cardAccount.put(-100);
        cardAccount.put(100);
        cardAccount.take(200);
        cardAccount.take(90);
        System.out.println("Осталось денег на счету - " + cardAccount.getAmount() + "р.");
        cardAccount.take(10);
        System.out.println("Осталось денег на счету - " + cardAccount.getAmount() + "р.");

        DepositAccount depositAccount = new DepositAccount(100.0);

        depositAccount.put(100);

        System.out.println("Осталось денег на счету - " + depositAccount.getAmount() + "р.");

        depositAccount.take(100);

        System.out.println("Осталось денег на счету - " + depositAccount.getAmount() + "р.");
    }
}
