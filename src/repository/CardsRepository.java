package repository;

import entity.Cards;
import util.ApplicationContext;

import javax.xml.transform.Source;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardsRepository {
    ApplicationContext ctx = ApplicationContext.getInstance();
    Connection connection = ctx.getConnection();

    public void insertCard(Cards card , int memberId){

        try {
            String sql = "insert into cards (member_id , name , number , sheba , amount)" +
                         " values (?,?,?,?,?)";
            PreparedStatement preS = connection.prepareStatement(sql);
            preS.setInt(1,memberId);
            preS.setString(2,card.getName());
            preS.setInt(3,card.getNumber());
            preS.setInt(4,card.getSheba());
            preS.setInt(5,card.getAmount());
            boolean execute = preS.execute();
            if (!execute){
                System.out.println("successful!");
            }else {
                System.out.println("unsuccessful!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
