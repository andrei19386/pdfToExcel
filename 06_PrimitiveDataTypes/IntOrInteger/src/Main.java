public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        container.addCount(5672);
        System.out.println(container.getCount());

        // TODO: ниже напишите код для выполнения задания:
        //  С помощью цикла и преобразования чисел в символы найдите все коды
        //  букв русского алфавита — заглавных и строчных, в том числе буквы Ё.
        /*
        Для поиска кодов русских букв воспользуемся тем, что все прописные буквы и все строчные буквы
        в таблице кодов расположены в строгом соответствии с русским алфавитом (за исключением буквы ё).
        Вначале найдем коды начальных и конечных букв алфавита а и я, а также прописных и строчных букв ё
         */
        int yoUppercaseCode = 0;
        int yoLowercaseCode = 0;
        int aUppercaseCode = 0;
        int aLowercaseCode = 0;
        int yaUppercaseCode = 0;
        int yaLowercaseCode = 0;
        int i = 0; //Переменная-параметр цикла
        char c; // Вспомогательная переменная

        for(i = 0; i < 65536; i++){ // Цикл по всей таблице символов
            c = (char)i;
            switch(c) {
                case 'Ё' -> { yoUppercaseCode = i; }
                case 'ё' -> { yoLowercaseCode = i; }
                case 'А' -> { aUppercaseCode = i;  }
                case 'а' -> { aLowercaseCode = i; }
                case 'Я' -> { yaUppercaseCode = i;  }
                case 'я' -> { yaLowercaseCode = i; }
            }
        }
        //Выведем полученные результаты на экран и убеждаемся, что все символы алфавита найдены
        System.out.println("Буква Ё - " + yoUppercaseCode);
        System.out.println("Буква ё - " + yoLowercaseCode);
        System.out.println("Буква А - " + aUppercaseCode);
        for(i = aUppercaseCode + 1; i < yaUppercaseCode; i++){
            c = (char)i;
            System.out.println("Буква " + c + " - " + i);
        }
        System.out.println("Буква Я - " + yaUppercaseCode);
        System.out.println("Буква а - " + aLowercaseCode);
        for(i = aLowercaseCode + 1; i < yaLowercaseCode; i++){
            c = (char)i;
            System.out.println("Буква " + c + " - " + i);
        }
        System.out.println("Буква я - " + yaLowercaseCode);
    }
}
