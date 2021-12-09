import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Company {

    private ArrayList<Worker> workers = new ArrayList<>();
    private int income;
    private int compareMode;

    protected void hire(Worker worker) {
        workers.add(worker);
        worker.setCompany(this);
        this.income += worker.getIncomeForCompany();
    }

    protected int getCompareMode() {
        return compareMode;
    }

    protected void hireAll(ArrayList<Worker> workersList){
        for(Worker item : workersList) {
            hire(item);
        }
    }

    protected void fire(int index){
        this.income -= workers.get(index).getIncomeForCompany();
        workers.remove(index);
    }

    protected int getIncome(){
        return income;
    }

    private List<Worker> getListSalaryStaff(int count, TreeSet<Worker> treeSet) {

        ArrayList<Worker> listSalaryWorkers = new ArrayList<>();
        int innerCount = 0;

        if (count >= 0 && count <= treeSet.size()) {
            for (Worker item : treeSet) {
                listSalaryWorkers.add(item);
                innerCount++;
                if (innerCount > count) {
                    break;
                }
            }
        }
        return listSalaryWorkers;
    }


    protected List<Worker> getTopSalaryStaff(int count) {

        compareMode = 1;
        TreeSet<Worker> treeSet = new TreeSet<>();
        treeSet.addAll(workers);

        return getListSalaryStaff(count, treeSet);
    }

    protected List<Worker> getLowestSalaryStaff(int count) {
        compareMode = -1;
        TreeSet<Worker> treeSet = new TreeSet<>();
        treeSet.addAll(workers);
        return getListSalaryStaff(count, treeSet);
    }

    protected ArrayList<Worker> getWorkers() {
        return workers;
    }
}
