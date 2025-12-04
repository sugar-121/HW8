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
            String sql1 = "SELECT name FROM members WHERE name = '"+ member.getUserName()+ "'";
            ResultSet resultSet = statement.executeQuery(sql1);
            if (!resultSet.next()) {
                String sql2 = "insert into members (name , password) values (?,?)";
                PreparedStatement preS = connection.prepareStatement(sql2);
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

    public int fetchMember(Members member){

        try {
            String sql = "select id from members where name = '" + member.getUserName()+"' and " +
                        "password = '" + member.getPassword() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
               return resultSet.getInt("id");
            }else {
                return -1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

