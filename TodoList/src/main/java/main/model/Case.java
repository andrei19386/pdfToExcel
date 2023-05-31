package main.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "case_")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private boolean isCompleted = false;
    private final LocalDate date = LocalDate.now();

    public Case() {
    }

    public Case(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public void inverse(){
        if(this.isCompleted){
            this.setCompleted(false);
        } else {
            this.setCompleted(true);
        }
    }

}
