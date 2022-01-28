import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//Класс тестирования
public class Main {
    public static void main(String[] args) {

        //Создаем счета
        Bank bank = new Bank();
        bank.createBankAccount("0000",170_000L);
        bank.createBankAccount("0001",90_000L);
        bank.createBankAccount("0010",80_000L);
        bank.createBankAccount("0011",160_000L);
        bank.createBankAccount("0100",110_000L);
        bank.createBankAccount("0101",95_000L);
        bank.createBankAccount("0110",130_000L);
        bank.createBankAccount("0111",105_000L);
        bank.createBankAccount("1000",1_115_000L);
        bank.createBankAccount("1001",75_000L);

        System.out.println("На всех аккаунтах в рублях: " + bank.getSumAllAccounts());


        //Генерируем случайные параметры передач
        List<ParameterSet> listOfParameters = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            ParameterSet parameterSet = new ParameterSet();
            listOfParameters.add(parameterSet);
        }

        // Запускаем потоки с операциями (150 потоков)
        for(ParameterSet parameterSet : listOfParameters) {
            new Thread( () -> {

                String fromNum = parameterSet.getFrom();
                String toNum = parameterSet.getTo();
                bank.transfer(fromNum, toNum, parameterSet.getAmount());
                synchronized (Main.class) {
                    System.out.println(Thread.currentThread().getName() + "______________________________");
                    System.out.println(Thread.currentThread().getName() + ": Передача денег со счета "
                            + fromNum + " на счет " + toNum + " на сумму "  + parameterSet.getAmount() + " руб.");
                    info(bank, fromNum);
                    info(bank,toNum);
                    System.out.println(Thread.currentThread().getName() + "______________________________");
                    for(Map.Entry<String,Account> entry : bank.getAccounts().entrySet()){
                        info(bank, entry.getKey());
                    }
                    System.out.println(Thread.currentThread().getName() + ": На всех аккаунтах в рублях: "
                            + bank.getSumAllAccounts());
                }
            }

            ).start();
        }
    }
    //Операция вывода информации на экран
    private static void info(Bank bank, String fromNum) {
        if(bank.getBalance(fromNum) > 0) {
            System.out.println(Thread.currentThread().getName() + ": Amount on  " + fromNum + " "
                    + bank.getBalance(fromNum));
        }
    }
}