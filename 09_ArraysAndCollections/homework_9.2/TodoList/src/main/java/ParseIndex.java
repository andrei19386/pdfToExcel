//Это вспомогательный класс для реализации команд EDIT и ADD с индексом.
//Выполняет разделение строки на две части: до пробела - целое число (индекс),
//после - строка для замены или добавления

public class ParseIndex {
    private final int index;
    private final String todo;

    public ParseIndex() {
        this.index = 0;
        this.todo = "";
    }

    public int getIndex() {
        return index;
    }

    public String getTodo() {
        return todo;
    }

    public ParseIndex(String data) {
        int firstSpaceIndex = data.indexOf(" ", 0);
        String indexString = data.substring(0,firstSpaceIndex);
        this.index = Integer.parseInt(indexString);
        this.todo = data.substring(firstSpaceIndex + 1);
    }

}
