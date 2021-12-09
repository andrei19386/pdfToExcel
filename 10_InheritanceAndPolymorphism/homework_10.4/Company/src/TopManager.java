public class TopManager extends Worker {

    public TopManager() {
        setFixedIncome(200_000);
        setIncomeForCompany(0);
    }

    @Override
    public int getMonthSalary() {
        Company company = this.getCompany();
        if ( company.getIncome() > 10_000_000) {
            return (int) (getFixedIncome() * 1.5);
        } else {
            return getFixedIncome();
        }
    }
}
