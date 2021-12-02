import java.util.Scanner;
import java.util.Set;

public class Main {
    private static PhoneBook phoneBook = new PhoneBook();

    public static void phoneAdditionBlock(Set<String> phones, String input) {
        if(phones.isEmpty()) {
            System.out.println("Такого имени в телефонной книге нет.\nВведите номер телефона для абонента "
                    + input + ":");
            Scanner scanner = new Scanner(System.in);
            String inputPhone = scanner.nextLine();
            String phoneFormatted = phoneBook.revisePhone(inputPhone,false);
            phoneBook.addContact(phoneFormatted, input);
        } else {
            String allPhones = phoneBook.getAllPhonesToString(input);
            System.out.println(allPhones);
        }
    }

    public static void nameAdditionBlock(String name, String phoneFormatted) {


        if( !name.equals("") ) {
            //  String allPhones = phoneBook.getAllPhonesToString(name);
            //  System.out.println(allPhones);
            System.out.println(name);
            System.out.println("Введите новое имя контакта:");
        } else {
            System.out.println("Такого номера нет в телефонной книге.\nВведите имя абонента для номера "
                    + name + ":");

        }
        Scanner scanner = new Scanner(System.in);
        String inputName = scanner.nextLine();
        phoneBook.addContact(phoneFormatted, inputName);
    }

    private static void print() {
        Set<String> resultSet = phoneBook.getAllContacts();
        for(String item : resultSet) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите номер, имя или команду:");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                break;
            }
            if (input.equals("LIST")){ // Вызов команды LIST
                print();
                System.out.println();
                continue;
            }
            if(phoneBook.reviseName(input,false)) { // Если ввод соответствует имени
                Set<String> phones = phoneBook.getContactByNameVersion2(input);
                phoneAdditionBlock(phones, input);
                System.out.println();
                continue;
            }
            String phoneFormatted = phoneBook.revisePhone(input,false);
            if(!phoneFormatted.equals("-1")) { //Если ввод соответствует номеру телефона
                String name = phoneBook.getContactByPhoneVersion2(phoneFormatted);
                nameAdditionBlock(name, phoneFormatted);
                System.out.println();
            } else {
                System.out.println("Неверный формат ввода");
            }
        }
    }
}
