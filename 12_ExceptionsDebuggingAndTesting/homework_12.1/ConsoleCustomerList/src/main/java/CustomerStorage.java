import java.util.HashMap;
import java.util.Map;

public class CustomerStorage {
    private final Map<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;

        String[] components = data.split("\\s+");
        extractedExceptions(INDEX_EMAIL, INDEX_PHONE, components);
        String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
        storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
    }

    private void extractedExceptions(int INDEX_EMAIL, int INDEX_PHONE, String[] components) {
        if(components.length != 4) {
            throw new IllegalArgumentException("Wrong format of input string! Please correct with format: " +
                    "add Василий Петров vasily.petrov@gmail.com +79215637722");
        }
        if(!components[INDEX_EMAIL].matches("[A-z1-9]+@[A-z1-9]+.[A-z]{2,3}")){
            throw new IllegalArgumentException("Wrong format of email! Please correct email");
        }
        if(!components[INDEX_PHONE].matches("[+][0-9]{11}")) {
            throw new IllegalArgumentException("Wrong format of phone! Phone number must consist of " +
                    "sign + and 11 digits! For example, +79261111111");
        }
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        if(getCustomer(name)==null) {
            throw new IllegalArgumentException("Customer does not exist!");
        }
        storage.remove(name);
    }

    public Customer getCustomer(String name) {
        return storage.get(name);
    }

    public int getCount() {
        return storage.size();
    }
}