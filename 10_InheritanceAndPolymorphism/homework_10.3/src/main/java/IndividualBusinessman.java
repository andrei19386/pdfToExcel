public class IndividualBusinessman extends Client {
    public static final double COMMISSION_AMOUNT_LESS_1000 = 1; //%
    public static final double COMMISSION_AMOUNT_BIGGER_1000 = 0.5; //%

    @Override
    protected void put(double amountToPut) {
        double commission;
        double withCommission = amountToPut;
        if(Double.compare(amountToPut, 0.0) >= 0) { // При попытке положить отрицательную сумму
            // нет смысла считать комиссию и выводить информацию о ней
            if(Double.compare(amountToPut,1000.0) < 0) {
                System.out.println("Пополнение с комиссией при сумме пополнения менее 1000 р. - " +
                        COMMISSION_AMOUNT_LESS_1000 + " %");
                commission = getCommission(amountToPut, COMMISSION_AMOUNT_LESS_1000);
            } else {
                System.out.println("Пополнение с комиссией при сумме пополнения больше или равно 1000 р. - " +
                        COMMISSION_AMOUNT_BIGGER_1000 + " %");
                commission = getCommission(amountToPut, COMMISSION_AMOUNT_BIGGER_1000);
            }
            withCommission = Double.sum(amountToPut, -commission );
        }
        super.put(withCommission);
    }
}
