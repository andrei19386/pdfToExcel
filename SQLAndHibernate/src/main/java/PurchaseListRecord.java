import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="purchaseList")
public class PurchaseListRecord {
    @EmbeddedId
    private KeyPurchase id;

    @Column(name = "student_name", insertable = false, updatable = false)
    private String studentName;

    @Column(name = "course_name", insertable = false, updatable = false)
    private String courseName;

    private int price;

    @Column(name="subscription_date")
    private Date subscriptionDate;

    public void setId(KeyPurchase id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public KeyPurchase getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
