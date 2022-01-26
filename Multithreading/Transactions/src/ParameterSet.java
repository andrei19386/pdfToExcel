public class ParameterSet {
    private String from;
    private String to;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public ParameterSet() {
        int fromInt;
        int toInt;
        while (true) {
            fromInt = (int) (Math.random() * 10);
            toInt = (int) (Math.random() * 10);
            if (fromInt != toInt) {
                break;
            }
        }
        amount = (int)(Math.random() * 52) * 1000 + 1000; // от 1000 до 52000, чтобы вероятность отправления
        // на проверку в службу безопасности была невысокой (в соответствии с условие задачи)
        from = convertToString(fromInt);
        to = convertToString(toInt);
    }
    private String convertToString(int number){
        switch (number) {
            case 0: return "0000";
            case 1: return "0001";
            case 2: return "0010";
            case 3: return "0011";
            case 4: return "0100";
            case 5: return "0101";
            case 6: return "0110";
            case 7: return "0111";
            case 8: return "1000";
            case 9: return "1001";
            default: return "XXXX";
        }
    }
}
