import util.ApplicationProperties;
import util.ApplicationContext;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.getInstance();
        Connection connection = ctx.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute("drop table users");
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}