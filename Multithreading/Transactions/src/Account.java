public class Account {

    private long money; //Денег на счету
    private final String accNumber; // Номер счета
    private volatile boolean isBlocked; // Заблокирован ли счет?
    private volatile boolean isTemporarilyBlocked; // Вспомогательная переменная для временной блокировки счета

    public Account(String accNumber, long money) {
        this.accNumber = accNumber;
        this.money = money;
        this.isBlocked = false;
        this.isTemporarilyBlocked = false;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
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
}
