import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Movement {
    private final String accountType;
    private final String accountNumber;
    private final String currency;
    private Date operationDate;
    private final String referenceWiring;
    private final String operationDescription;
    private final double incomeSum;
    private final double expenseSum;

    public Movement(List<String> fragments) {
        this.accountType = fragments.get(0);
        this.accountNumber = fragments.get(1);
        this.currency = fragments.get(2);
        try {
            this.operationDate = new SimpleDateFormat("dd.MM.yyyy").parse(fragments.get(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.referenceWiring = fragments.get(4);
        this.operationDescription = fragments.get(5);
        if(fragments.get(6).matches("[0-9]+[.][0-9]+") || fragments.get(6).matches("[0-9]+")) {
            this.incomeSum = Double.parseDouble(fragments.get(6));
        } else {
            throw new NumberFormatException("Invalid Number Type for variable incomeSum!");
        }
        if(fragments.get(7).matches("[0-9]+[.][0-9]+") || fragments.get(7).matches("[0-9]+")) {
            this.expenseSum = Double.parseDouble(fragments.get(7));
        } else {
            throw new NumberFormatException("Invalid Number Type for variable expenseSum!");
        }
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public String getReferenceWiring() {
        return referenceWiring;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public double getIncomeSum() {
        return incomeSum;
    }

    public double getExpenseSum() {
        return expenseSum;
    }
}
