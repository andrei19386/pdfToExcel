import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();

        /*Course course = session.get(Course.class,1);
        List<Student> studentList = course.getStudents();
        studentList.forEach(s -> System.out.println(s.getName()));
        Transaction transaction = session.beginTransaction();

        transaction.commit();*/

        PurchaseListRecord purchaseListRecord = session.get(PurchaseListRecord.class,
                new KeyPurchase("Круминьш Виталий", "Управление продуктом"));
        System.out.println(purchaseListRecord.getSubscriptionDate());

        Student student = session.get(Student.class,1);
        List<Subscription> subscriptionList = student.getSubscriptions();
        subscriptionList.forEach(s->{
           System.out.println(s.getCourse().getName());
        });


        sessionFactory.close();
    }
}
