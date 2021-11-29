public class TwoDimensionalArray {
    public static char symbol = 'X';
    public static char space = ' ';

    public static char[][] getTwoDimensionalArray(int size) {

        if(size <= 0){
            System.out.println("Некорректно введены данные!");
            char [][] array = { { ' '} };
            return array;
        }


        char [][] array = new char[size][size];

        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i == j || i == size - 1 - j) {
                    array[i][j] = symbol;
                } else {
                    array[i][j] = space;
                }
            }
        }
        return array;
    }
}
