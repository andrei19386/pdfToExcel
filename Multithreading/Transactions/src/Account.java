public class Account implements Comparable<Account>{

    private volatile long money; //Денег на счету
    private final String accNumber; // Номер счета
    private volatile boolean isBlocked; // Заблокирован ли счет?
    private volatile boolean isTemporarilyBlocked; // Вспомогательная переменная для временной блокировки счета

    public Account(String accNumber, long money) {
        this.accNumber = accNumber;
        this.money = money;
        this.isBlocked = false;
        this.isTemporarilyBlocked = false;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean getIsTemporarilyBlocked() {
        return isTemporarilyBlocked;
    }

    public void setTemporarilyBlocked(boolean temporarilyBlocked) {
        isTemporarilyBlocked = temporarilyBlocked;
    }

    @Override
    public int compareTo(Account o) {
        return this.getAccNumber().compareTo(o.getAccNumber());
    }
}
