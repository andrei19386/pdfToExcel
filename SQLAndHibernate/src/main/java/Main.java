import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
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

        transaction.commit();

        PurchaseListRecord purchaseListRecord = session.get(PurchaseListRecord.class,
                new KeyPurchase("Круминьш Виталий", "Управление продуктом"));
        System.out.println(purchaseListRecord.getSubscriptionDate());

        Student student = session.get(Student.class,1);
        List<Subscription> subscriptionList = student.getSubscriptions();
        subscriptionList.forEach(s->{
           System.out.println(s.getCourse().getName());
        });
        */
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PurchaseListRecord> query = builder.createQuery(PurchaseListRecord.class);
        Root<PurchaseListRecord> root = query.from(PurchaseListRecord.class);
        query.select(root);
        List<PurchaseListRecord> purchaseListRecordList = session.createQuery(query).getResultList();


        Transaction transaction = session.beginTransaction();

        for(PurchaseListRecord record: purchaseListRecordList) {
            String hqlStudent = "From " + Student.class.getSimpleName() + " Where name = '" + record.getStudentName() +"'";
            String hqlCourse = "From " + Course.class.getSimpleName() + " Where name = '" + record.getCourseName() + "'";
            Student studentRec = (Student) session.createQuery(hqlStudent).getSingleResult();
            Course courseRec = (Course) session.createQuery(hqlCourse).getSingleResult();

            LinkedPurchaseListRecord linkedPurchaseListRecord = new LinkedPurchaseListRecord();
            linkedPurchaseListRecord.setStudentId(studentRec.getId());
            linkedPurchaseListRecord.setCourseId(courseRec.getId());
            session.save(linkedPurchaseListRecord);
        }

        transaction.commit();
        sessionFactory.close();
    }
}
