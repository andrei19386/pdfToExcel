public abstract class Worker implements Employee, Comparable<Worker>
{
    private int fixedIncome;
    private Company company;
    private int incomeForCompany;

    protected void setIncomeForCompany(int incomeForCompany) {
        this.incomeForCompany = incomeForCompany;
    }

    protected int getIncomeForCompany() {
        return incomeForCompany;
    }

    protected Company getCompany() {
        return company;
    }

    protected void setCompany(Company company) {
        this.company = company;
    }

    protected int getFixedIncome() {
        return fixedIncome;
    }

    protected void setFixedIncome(int fixedIncome) {
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
