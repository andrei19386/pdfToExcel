public class Container {
    private int count;
    //int - примитив, значение по умолчанию равно 0; Integer - класс-обертка, адрес по умолчанию равен NULL
    //В данном случае, чтобы ошибка не возникала, нужно написать int, чтобы к значению 0 прибавить value
    public void addCount(int value) {
        count = count + value;
    }

    public int getCount() {
        return count;
    }
}
