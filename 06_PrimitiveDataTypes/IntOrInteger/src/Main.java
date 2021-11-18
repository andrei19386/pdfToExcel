public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        container.addCount(5672);
        System.out.println(container.getCount());

        // TODO: ниже напишите код для выполнения задания:
        //  С помощью цикла и преобразования чисел в символы найдите все коды
        //  букв русского алфавита — заглавных и строчных, в том числе буквы Ё.


        //Буква Ё
        int i = 1025;
        char c = (char)i;
        System.out.println(i + " - " + c);

//Остальные заглавные и строчные буквы русского алфавита
        //1040 - 1071 - прописные буквы
        //1072 - 1103 - строчные буквы
        for(i = 1040; i < 1104; i++){
            c = (char)i;
            System.out.println(i + " - " + c);
        }

        //Буква ё
        i = 1105;
        c = (char)i;
        System.out.println(i + " - " + c);

    }
}
