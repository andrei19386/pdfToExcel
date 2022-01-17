import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class Main {
    public static void main(String[] args) {
        // Основное задание
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Course course = session.get(Course.class,2);
        System.out.println(course.getName() +  " - " + course.getStudentsCount());


        //Дополнительное задание - необходимо раскомментировать цикл ниже, выводит записи 1-10 таблицы teachers
        /*
        for(int i = 1; i <= 10; i++) {
            Teacher teacher = session.get(Teacher.class, i);
            System.out.println("Id = " + teacher.getId() +
                    ", name = " + teacher.getName() +
                    ", salary = " + teacher.getSalary() +
                    ", age = " + teacher.getAge());
        }
        */

        sessionFactory.close();
    }
}
