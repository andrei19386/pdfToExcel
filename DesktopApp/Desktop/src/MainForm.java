import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton collapseButton;
    private JLabel label;
    private JLabel label2;
    private JLabel label3;
    private JLabel labelMain;
    private boolean isCollapsed;


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MainForm() {
        isCollapsed = false;

        collapseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isCollapsed) {
                    toCollapse();
                } else {
                    toExpand();
                }
            }
        });
    }

    private void toExpand() {
        String fullName = textField1.getText();
        String[] words = fullName.strip().split(" ");
        if (words.length == 2) {
            actionOnElements(words);
            textField3.setText("");

        } else if (words.length == 3) {
            actionOnElements(words);
            textField3.setText(words[2]);
        } else {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Неверное количество слов в поле, должно быть 2 или 3 слова",
                    "Неверный ввод",
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }

    private void actionOnElements(String[] words) {
        labelMain.setText("Введите данные в поля ниже и нажмите кнопку." +
                " Поля, помеченные *, обязательны к заполнению");
        label.setText("Введите фамилию: *");
        label2.setVisible(true);
        label3.setVisible(true);
        textField1.setText(words[0]);
        textField2.setText(words[1]);
        textField2.setVisible(true);
        textField3.setVisible(true);
        collapseButton.setText("Collapse");
        isCollapsed = false;
    }


    private void toCollapse() {
        String surname = textField1.getText();
        String name = textField2.getText();
        String patronymic = textField3.getText();
        if(surname.strip().matches("[А-Я][а-я]+") && name.strip().matches("[А-Я][а-я]+")) {
            if (patronymic.matches("[А-Я][а-я]+") ||
                    patronymic.strip().matches("")) {
                        String fullName = surname.strip() + " " + name.strip() + " " + patronymic.strip();
                        label.setText("Введите фамилию (обязательно), имя (обязательно), отчество");
                        label2.setVisible(false);
                        label3.setVisible(false);
                        textField1.setText(fullName);
                        textField2.setVisible(false);
                        textField3.setVisible(false);
                        collapseButton.setText("Expand");
                        isCollapsed = true;
                        labelMain.setText("Введите данные в поле ниже и нажмите кнопку");
            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Неверно введено отчество",
                        "Неверный ввод",
                        JOptionPane.PLAIN_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Неверно введена фамилия или имя",
                    "Неверный ввод",
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }

}
