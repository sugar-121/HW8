package repository;


import entity.Members;
import util.ApplicationContext;

import java.sql.*;

public class MemberRepository {
    ApplicationContext ctx = ApplicationContext.getInstance();
    Connection connection = ctx.getConnection();

    public boolean regMember(Members member) {
        boolean flag = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM members WHERE name ="+"'"+ member.getUserName()+"'");

            if (!resultSet.next()) {
                String sql = "insert into members (name , password) values (?,?)";
                PreparedStatement preS = connection.prepareStatement(sql);
                preS.setString(1, member.getUserName());
                preS.setInt(2, member.getPassword());
                preS.executeUpdate();
                flag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return flag;
    }


}

