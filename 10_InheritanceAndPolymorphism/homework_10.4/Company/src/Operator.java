public class Operator extends Worker {

    public Operator(int fixedIncome){
        setFixedIncome(fixedIncome);
        setIncomeForCompany(0);
    }

    public Operator(){
        setFixedIncome(50_000);
        setIncomeForCompany(0);
    }

    @Override
    public int getMonthSalary() {
        return getFixedIncome();
    }

}
