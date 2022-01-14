import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "123456789";
        String query = "SELECT course_name, count(*)/8 AS Average_count_per_month FROM purchaselist \n" +
                "WHERE year(subscription_date) = 2018 \n" +
                "GROUP BY course_name";
        try {
            Connection connection = DriverManager.getConnection(url,user,pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("course_name - Average_count_per_month");
            while(resultSet.next()) {
                String name  = resultSet.getString("course_name");
                double averageCount = resultSet.getDouble("Average_count_per_month");
                System.out.println(name + "  -  " + averageCount);
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }
}
