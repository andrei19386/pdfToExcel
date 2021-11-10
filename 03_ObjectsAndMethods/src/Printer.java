public class Printer {
    private String queue;
    private int pendingPageCount;
    private int printedPageCount;

    public Printer() { //Конструктор класса принтер
        this.queue = "";
        this.pendingPageCount = 0;
        this.printedPageCount = 0;
    }

        public void append(String documentText, String documentName, int pageCount) { //Добавление в очередь печати на случай трех параметров
            queue = queue + " Text = " + documentText + " Name = " + documentName + " pageCount = " +  pageCount + "\n";
            pendingPageCount += pageCount;
        }

        public void append(String documentText, String documentName) { //Добавление в очередь печати на случай двух параметров
            append(documentText, documentName, 1);
        }

        public void append(String documentText) { //Добавление в очередь печати обязательного параметра
            append(documentText, "!WithoutName!");
        }





        public void clear() { //Очистка очереди печати
            queue = "";
            pendingPageCount = 0;
        }

        public int getPendingPagesCount(){
            return pendingPageCount;
        }

        public void print() { //Печать

            if (queue.isEmpty()) {
                System.out.println("Очередь пуста");
            } else {
                printedPageCount += getPendingPagesCount();
                System.out.println(queue);
                clear();
            }
        }



        public int getPrintedPagesCount(){//Подсчет
            //	общего количества напечатанных страниц с момента создания объекта
            return printedPageCount;
        }
}
