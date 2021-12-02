import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;

public class PhoneBook {

    Map<String,String> phonebook = new TreeMap<>();

    public boolean reviseName(String name, boolean printAllowed) {
        boolean match = name.matches("[А-Я][а-я_0-9]+");
        if(!match && printAllowed) {
            System.out.println("Неверный формат имени!");
        }
        return match;
    }

    public String revisePhone(String phone, boolean printAllowed) {
        String output;
        String regex = "[^0-9]";
        output = phone.replaceAll(regex,"");
        String regexSecond = "[7-8][0-9]{10}";
        String regexThird = "[0-9]{10}"; //Все номера приводим к такому формату для хранения в Map в качестве ключа
        if (output.matches(regexSecond)) {
            output = output.replaceFirst("[78]","");
        }
        if (output.matches(regexThird)) {
            return (7 + output);
        } else if(printAllowed) {
            System.out.println("Неверный формат номера!");
        }
        return "-1";
    }

    public void addContact(String phone, String name) {
        if( reviseName(name,true) && !revisePhone(phone,true).equals("-1") ) {
            if ( phonebook.containsKey(phone) ) {
                phonebook.replace(phone, name);
            } else {
                phonebook.put(phone, name);
            }
            System.out.println("Контакт сохранен!");
        }
    }

    public String getContactByPhone(String phone) {
        if( phonebook.containsKey(phone) ) {
            String value = phonebook.get(phone);
            System.out.println(value);
            return value + " - " + phone;
        }
        return "";
    }

    public Set<String> getContactByName(String name) {
        TreeSet<String> treeSet = new TreeSet<>();

        for (Map.Entry<String, String> entry : phonebook.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if( value.equals(name) ) {
                treeSet.add(name + " - " + key);
            }
        }
        return treeSet;
    }

    public Set<String> getAllContacts() {
        TreeSet<String> treeSet = new TreeSet<>();

        for (Map.Entry<String, String> entry : phonebook.entrySet()) {
            String value = entry.getValue();
            String result = getAllPhonesToString(value);
            treeSet.add(result);
        }
        return treeSet;
    }

    public String getAllPhonesToString(String name) {

        Set<String> temporaryTreeSet = getContactByNameVersion2(name);
        String result = name + " - ";
        boolean isNotFirst = false;
        for(String item : temporaryTreeSet) {
            if (isNotFirst) {
                result = result + ", ";
            }
            result = result + item;
            isNotFirst = true;
        }
        return result;
    }

    public Set<String> getContactByNameVersion2(String name) {
        TreeSet<String> treeSet = new TreeSet<>();

        for (Map.Entry<String, String> entry : phonebook.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if( value.equals(name) ) {
                treeSet.add(key);
            }
        }
        return treeSet;
    }

    public String getContactByPhoneVersion2(String phone) {
        if( phonebook.containsKey(phone) ) {
            String value = phonebook.get(phone);
            return value;
        }
        return "";
    }
}