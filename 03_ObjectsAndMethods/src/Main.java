public class Main {

    public static void main(String[] args) {

        System.out.println("##########___HOMEWORK1___ ##########");
        Basket basket = new Basket();
        basket.add("Milk", 40, 3,120);
        basket.add("Sugar",30,1,15);
        basket.add("Water",10,1);
        basket.print("Milk");
        System.out.println("Общий вес покупок - " + basket.getTotalWeight() + " г");
        System.out.println("\n\n\n\n\n\n");

        System.out.println("##########___HOMEWORK2___##########");


        Arithmetic arithmetic = new Arithmetic(5, 7); //Создание объекта класса
        System.out.println("Сумма двух чисел равна " + arithmetic.sum());
        System.out.println("Произведение двух чисел равно " + arithmetic.multiplication());
        System.out.println("Максимальное из двух чисел равно " + arithmetic.maxValue());
        System.out.println("Минимальное из двух чисел равно " + arithmetic.minValue());
        System.out.println("\n\n\n\n\n\n");
        System.out.println("##########___HOMEWORK3___##########");



        Printer printer = new Printer();

        for (int i = 0; i < 2; i++ ) { //Цикл написан для проверки работоспособности подсчета
            //	общего количества напечатанных страниц с момента создания объекта
            printer.append("Text1");
            printer.append("Text2", "Name2");
            printer.append("Text3", "Name3", 7);
            System.out.println("Количество страниц в ожидании печати " + printer.getPendingPagesCount() + "\n");
            printer.print();
            System.out.println("Всего страниц напечатано " + printer.getPrintedPagesCount() + "\n");
        }

    }
}
