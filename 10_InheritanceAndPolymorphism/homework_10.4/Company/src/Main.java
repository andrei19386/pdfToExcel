import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Worker> list = new ArrayList<>();
        Company company = new Company();
//Нанимаем 180 операторов в компанию
        for(int i = 0; i < 180; i++) {
            Operator operator = new Operator();
            company.hire(operator);
        }
//Нанимаем 80 менеджеров в компанию
        for(int i = 0; i < 80; i++) {
            Manager manager = new Manager();
            company.hire(manager);
        }
//Нанимаем 10 топ-менеджеров в компанию
        for(int i = 0; i < 10; i++) {
            TopManager topManager = new TopManager();
            company.hire(topManager);
        }

        System.out.println("10 самых высоких зарплат в компании");
        list = company.getTopSalaryStaff(10);
        if (!list.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                System.out.println((list.get(i)).getMonthSalary() + " руб.");
            }
        }
        System.out.println("30 самых низких зарплат в компании");
        list = company.getLowestSalaryStaff(30);
        if (!list.isEmpty()) {
            for (int i = 0; i < 30; i++) {
                System.out.println((list.get(i)).getMonthSalary() + " руб.");
            }
        }
        System.out.println();
        //Увольняем половину сотрудников
        int companySize = company.getWorkers().size(); //Фиксируем размер компании, поскольку
        // он будет дальше изменяться
        for (int i = 0; i < companySize / 2; i++) {
            company.fire( i ); //В связи с тем, что при удалении в List происходит сдвиг элементов,
            //удаление происходит через одного работника
        }

        System.out.println("10 самых высоких зарплат в компании после увольнения сотрудников");
        list = company.getTopSalaryStaff(10);
        if (!list.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                System.out.println((list.get(i)).getMonthSalary() + " руб.");
            }
        }

        System.out.println("30 самых низких зарплат в компании после увольнения сотрудников");
        list = company.getLowestSalaryStaff(30);
        if (!list.isEmpty()) {
            for (int i = 0; i < 30; i++) {
                System.out.println((list.get(i)).getMonthSalary() + " руб.");
            }
        }
    }
}
