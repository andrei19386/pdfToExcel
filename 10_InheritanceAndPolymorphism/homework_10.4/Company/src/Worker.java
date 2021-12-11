public abstract class Worker implements Employee, Comparable<Worker>
{
    private int fixedIncome;
    private Company company;
    private int incomeForCompany;

    public void setIncomeForCompany(int incomeForCompany) {
        this.incomeForCompany = incomeForCompany;
    }

    public int getIncomeForCompany() {
        return incomeForCompany;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getFixedIncome() {
        return fixedIncome;
    }

    public void setFixedIncome(int fixedIncome) {
        this.fixedIncome = fixedIncome;
    }

    @Override
    public int compareTo(Worker worker) {
        int compareMode = this.company.getCompareMode();
        if(getMonthSalary() < worker.getMonthSalary()){
            return compareMode;
        }
        if(getMonthSalary() > worker.getMonthSalary()){
            return -compareMode;
        }
        return 0;
    }
}
