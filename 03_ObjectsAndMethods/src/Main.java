public class Main {

    public static void main(String[] args) {
    /*
        System.out.println("##########___HOMEWORK1___ ##########");
        Basket basket = new Basket();
        basket.add("Milk", 40, 3,120);
        basket.add("Sugar",30,1,15);
        basket.add("Water",10,1);
        basket.print("____");
        System.out.println("Общий вес покупок - " + basket.getTotalWeight() + " г");

        Basket basketSecond = new Basket();
        basketSecond.add("Milk", 40, 7,120);
        basketSecond.add("Sugar",30,5,15);
        basketSecond.add("Butter",50,2);

        System.out.println();
        basketSecond.print("____");

        System.out.println("Общий вес покупок - " + basketSecond.getTotalWeight() + " г");

        System.out.println();
        System.out.println("Общая стоимость товаров по корзинам - " + Basket.getAllTotalPrice() + " р.");
        System.out.println("Общее количество товаров по корзинам - " + Basket.getAllGoods() + " р.");
        System.out.println("Общее количество корзин - " + Basket.getCount());
        System.out.println("Средняя цена товара во всех корзинах - " + Basket.averagePriceGoods() + " р.");
        System.out.println("Средняя цена одной корзины - " + Basket.averagePriceBasket() + " р.");
         */
        
        System.out.println("##########___HOMEWORK2___ ##########");
        ArithmeticCalculator arithmeticCalculator = new ArithmeticCalculator(43,10);
        System.out.println("Первое число в конструкторе калькулятора равно " + arithmeticCalculator.getVariableFirst());
        System.out.println("Второе число в конструкторе калькулятора равно " + arithmeticCalculator.getVariableSecond());
        System.out.println("Сумма чисел равна " + arithmeticCalculator.calculate(Operation.ADD));
        System.out.println("Разность чисел равна " + arithmeticCalculator.calculate(Operation.SUBTRACT));
        System.out.println("Произведение чисел равно " + arithmeticCalculator.calculate(Operation.MULTIPLY));


/*
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
*/
    }
}
