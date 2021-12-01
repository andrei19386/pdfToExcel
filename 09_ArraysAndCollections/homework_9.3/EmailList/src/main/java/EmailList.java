import java.util.*;

public class EmailList {
    private TreeSet<String> set = new TreeSet<>();
    public static final String WRONG_EMAIL_ANSWER = "Неверный формат email";
    public static final String EMAIL_EXISTS = "Такой email уже существует!";

    public void add(String email) {
        if( email.matches("[A-z_0-9]+[@][A-z]+[.][A-z]{2,3}") ) {
            if( set.add(email.toLowerCase()) ) {
                System.out.println("Email " + email + " добавлен!");
            } else {
                System.out.println(EMAIL_EXISTS);
            }
        } else {
            System.out.println(WRONG_EMAIL_ANSWER);
        }
    }

    public ArrayList<String> getSortedEmails() {
        ArrayList<String> result = new ArrayList<>();
        for(String item : set) {
            System.out.println(item);
            result.add(item);
        }
        return result;
    }
}
