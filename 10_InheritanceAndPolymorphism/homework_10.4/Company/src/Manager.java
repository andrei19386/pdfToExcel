public class Manager extends Worker {

    public Manager() {
        setFixedIncome(100_000);
        int incomeForCompany = (int)(Math.random()*25_000 + 115_000);
        setIncomeForCompany(incomeForCompany);
    }

    public Manager(int fixedIncome) {
        int incomeForCompany = (int)(Math.random()*25_000 + 115_000);
        setFixedIncome(fixedIncome);
        setIncomeForCompany(incomeForCompany);
    }

    @Override
    public int getMonthSalary() {
        return getIncomeForCompany() * 5 / 100 + getFixedIncome();
    }

}
