import java.util.List;

public class Main {

    public static final String STAFF_TXT = "data/staff.txt";

    public static void main(String[] args) {
        List<Employee> staff = Employee.loadStaffFromFile(STAFF_TXT);
        sortBySalaryAndAlphabet(staff);
        System.out.println(staff);
    }

    public static void sortBySalaryAndAlphabet(List<Employee> staff) {
        staff.sort((o1, o2) -> {
            int resultCompareBySalary = o1.getSalary().compareTo(o2.getSalary());
            if (resultCompareBySalary == 0) { //При равных зарплатах сортировка по имени
                return o1.getName().compareTo(o2.getName());
            } else {
                return resultCompareBySalary;
            }
        });
    }
}
