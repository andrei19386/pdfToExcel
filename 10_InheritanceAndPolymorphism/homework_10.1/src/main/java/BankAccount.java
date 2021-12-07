public class BankAccount {

  private double amount;

  public BankAccount() {
    this.amount = 0;
  }

  public BankAccount(double amount) {
    this.amount = amount;
  }

  protected double getAmount() {
    return amount;
  }

  protected void put(double amountToPut) {
    if (Double.compare(amountToPut,0.0) < 0 ) {
      System.out.println("Вы пытаетесь положить на счет отрицательную сумму денег!");
      return;
    }
    amount += amountToPut;
  }



  protected boolean take(double amountToTake) {
    if (Double.compare(amountToTake,amount) > 0) {
      System.out.println("Недостаточно средств на счете!");
      return false;
    } else {
      amount -= amountToTake;
      return true;
    }
  }


  protected boolean send(BankAccount receiver, double amount) {
    boolean isSuccess = this.take(amount);
    if(isSuccess) {
      receiver.put(amount);
    }
    return isSuccess;
  }
}
