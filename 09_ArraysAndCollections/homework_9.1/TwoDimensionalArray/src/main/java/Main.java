public class Main {
    public static void main(String[] args) {
        int size = 7;
        char[][] array = TwoDimensionalArray.getTwoDimensionalArray(size);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }
}
