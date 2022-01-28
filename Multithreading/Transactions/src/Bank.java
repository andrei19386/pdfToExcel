import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {

    public static final long NO_CHECKED_SUM = 50_000L;

    private Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {

        Thread.sleep(1000);
        return random.nextBoolean();
    }


    public void  transfer(String fromAccountNum, String toAccountNum, long amount) {

        if(accounts.containsKey(fromAccountNum) && accounts.containsKey(toAccountNum)) {
            if(!accounts.get(fromAccountNum).getIsBlocked() &&
                    !accounts.get(toAccountNum).getIsBlocked()
            ) {
                waitAndGetResults(fromAccountNum, toAccountNum, amount);//Если счета не заблокированы, перейти
                // к дальнейшей обработке
            } else {
                System.out.println(Thread.currentThread().getName() +
                        ": Cannot execute the operation, one of accounts is blocked!");
            }
        } else {
            System.out.println("Cannot execute the operation, one of accounts is not found!");
        }
    }

    private void waitAndGetResults(String fromAccountNum, String toAccountNum, long amount) {
        waiting(fromAccountNum, toAccountNum);
        transferCheck(amount, accounts.get(fromAccountNum), accounts.get(toAccountNum));

        unblocking(fromAccountNum, toAccountNum);
    }

    private synchronized void waiting(String fromAccountNum, String toAccountNum) {
        {
            if (accounts.get(fromAccountNum).getIsTemporarilyBlocked() ||
                    accounts.get(toAccountNum).getIsTemporarilyBlocked()) {
                try {
                    wait();//Если счета временно заблокированы до проверки службой безопасности,
                    // ждем разблокировки
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void unblocking(String fromAccountNum, String toAccountNum) {
        //Сняли временную блокировку
        accounts.get(fromAccountNum).setTemporarilyBlocked(false);
        accounts.get(toAccountNum).setTemporarilyBlocked(false);
        notifyAll(); //Отправили оповещение другим потокам
    }

    private void transferCheck(long amount, Account from, Account to) {
        String fromAccountNum = from.getAccNumber();
        String toAccountNum = to.getAccNumber();

        if (amount > NO_CHECKED_SUM) {
            checkTransaction(amount, from, to, fromAccountNum, toAccountNum);
        } else {
            transferExecute(amount, from, to); //Переводим без проверки транзакции
        }
    }

    private void checkTransaction(long amount, Account from, Account to, String fromAccountNum, String toAccountNum) {
        //Устанавливаем временную блокировку на проверяемые счета

        from.setTemporarilyBlocked(true);
        to.setTemporarilyBlocked(true);
        try {
            if (isFraud(fromAccountNum, toAccountNum, amount)) {
                //Если обнаружено мошенничество, устанавливаем постоянную блокировку на счета
                from.setBlocked(true);
                to.setBlocked(true);
            } else {
                transferExecute(amount, from, to);//Если не мошенничество, выполняем передачу между счетами
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void transferExecute(long amount, Account from, Account to) {
        synchronized (from) {
            if (from.getMoney() >= amount) {
                {
                    from.setMoney(from.getMoney() - amount);
                    putMoney(amount,from,to);
                }

            } else {
                System.out.println(Thread.currentThread().getName() + ": Nothing to do! No enough money on the account "
                        + from.getAccNumber() + " !");
            }
        }
    }

    private void putMoney(long amount, Account from, Account to){
        synchronized (to.compareTo(from) > 0 ? to : from) {
            to.setMoney(to.getMoney() + amount);
        }
    }



    public long getBalance(String accountNum) {
        if(accounts.containsKey(accountNum)) {
            Account account = accounts.get(accountNum);
            if(!account.getIsBlocked()) {
                return account.getMoney();
            } else {
                System.out.println(Thread.currentThread().getName() + ": Account " + accountNum + " is blocked!");
                return -1L;
            }
        }
        System.out.println("Account " + accountNum + " is not found!");
        return -2L;
    }

    public long getSumAllAccounts() {
        long sum = 0;
        for(Map.Entry<String,Account> entry : accounts.entrySet() )
        {
            sum += entry.getValue().getMoney();
        }
        return sum;
    }

    public void createBankAccount(String accNumber, long money) {
        Account account = new Account(accNumber,money);
        accounts.put(account.getAccNumber(),account);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }
}

