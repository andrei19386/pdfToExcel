import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Company {

    private List<Worker> workers = new ArrayList<>();
    private int income;
    private int compareMode;

    public void hire(Worker worker) {
        workers.add(worker);
        worker.setCompany(this);
        this.income += worker.getIncomeForCompany();
    }

    public int getCompareMode() {
        return compareMode;
    }

    public void hireAll(ArrayList<Worker> workersList){
            workersList.addAll(workersList);
    }

    public void fire(int index){
        this.income -= workers.get(index).getIncomeForCompany();
        workers.remove(index);
    }

    public int getIncome(){
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


    public List<Worker> getTopSalaryStaff(int count) {

        compareMode = 1;
        return getWorkers(count);
    }

    public List<Worker> getLowestSalaryStaff(int count) {
        compareMode = -1;
        return getWorkers(count);
    }

    private List<Worker> getWorkers(int count) {
        TreeSet<Worker> treeSet = new TreeSet<>();
        treeSet.addAll(workers);
        return getListSalaryStaff(count, treeSet);
    }

    public List<Worker> getWorkers() {
        return workers;
    }
}
