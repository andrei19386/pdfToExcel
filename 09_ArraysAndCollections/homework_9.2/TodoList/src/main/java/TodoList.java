import java.util.ArrayList;

public class TodoList {
    private ArrayList<String> list = new ArrayList<>();

    public void add(String todo) {
        list.add(todo);
        System.out.println("Добавлено дело \"" + todo + "\"");
    }

    public void add(int index, String todo) {
        if (index >= 0 && index < list.size()) {
            list.add(index, todo);
            System.out.println("Добавлено дело \"" + todo + "\"  с индексом " + index);
        } else {
            add(todo);
        }
    }

    public void edit(String todo, int index) {
        if (index >= 0 && index < list.size()) {
            String nameTodoLast = list.get(index);
            list.remove(index);
            list.add(index,todo);
            System.out.println("Дело \"" + nameTodoLast + "\" заменено на \"" + todo + "\"");
        } else {
            System.out.println("Дело с таким индексом не существует!");
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < list.size()) {
            String nameTodoLast = list.get(index);
            list.remove(index);
            System.out.println("Дело \"" + nameTodoLast + "\" удалено");
        } else {
            System.out.println("Дело с таким номером не существует!");
        }
    }

    public ArrayList<String> getTodos() {
        for(int i = 0; i < list.size(); i++){
            System.out.println(i + " - " + list.get(i) );
        }
        return list;
    }
 }