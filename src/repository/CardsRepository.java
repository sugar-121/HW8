package repository;

import entity.Cards;
import util.ApplicationContext;

import java.sql.*;

public class CardsRepository {
    ApplicationContext ctx = ApplicationContext.getInstance();
    Connection connection = ctx.getConnection();

    public void insertCard(Cards card, int memberId) {
        try {
            String sql = "insert into cards (member_id , name , number , sheba , amount)" +
                    " values (?,?,?,?,?)";
            PreparedStatement preS = connection.prepareStatement(sql);
            preS.setInt(1, memberId);
            preS.setString(2, card.getName());
            preS.setInt(3, card.getNumber());
            preS.setInt(4, card.getSheba());
            preS.setInt(5, card.getAmount());
            boolean execute = preS.execute();
            if (!execute) {
                System.out.println("successful!");
            } else {
                System.out.println("unsuccessful!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCardByName(String name, int memberId) {
        try {
            String sql = "select id , name , amount from  cards where member_id = ? and name = ?";
            PreparedStatement preS = connection.prepareStatement(sql);
            preS.setInt(1, memberId);
            preS.setString(2, name);
            ResultSet resultSet = preS.executeQuery();
            while (resultSet.next()) {
                System.out.println("card id: " + resultSet.getInt("id"));
                System.out.println("amount: " + resultSet.getString("amount"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCardById(int id) {
        try {
            String sql = " delete from cards where id = " + id;
            Statement s = connection.createStatement();
            int deletedRow = s.executeUpdate(sql);
            if (deletedRow > 0) {
                System.out.println("Successfully deleted.");
            } else {
                System.out.println("Such id doesnt exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showCardByNumber(int number, int memberId) {
        try {
            String sql = "select id , name , number, sheba , amount from cards where number = " + number + " and member_id = " + memberId;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            if (resultSet.next()) {
                System.out.println("id: " + resultSet.getInt("id"));
                System.out.println(" Bank: " + resultSet.getString("name"));
                System.out.println(" number: " + resultSet.getInt("number"));
                System.out.println(" sheba: " + resultSet.getInt("sheba"));
                System.out.println(" amount: " + resultSet.getInt("id"));
            } else {
                System.out.println("Number is wrong!!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAllCards(int memberId) {
        try {
            String sql = "select id , name , number, sheba , amount from cards where member_id = " + memberId;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            while (resultSet.next()) {
                System.out.print("card id: " + resultSet.getInt("id"));
                System.out.print(" | bank: " + resultSet.getString("name"));
                System.out.print(" | number: " + resultSet.getInt("number"));
                System.out.print(" | sheba: " + resultSet.getInt("sheba"));
                System.out.println(" | amount: " + resultSet.getString("amount"));
                System.out.println("----------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int fetchCardAmountByNumber(int cardNumber) {
        try {
            String sql = "select amount from cards where number = " + cardNumber;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("amount");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    public int fetchCardIdByNumber(int cardNumber) {
        try {
            String sql = "select id from cards where number = " + cardNumber;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
        return -1;
        }

//    public void updateAmount(int )
    }
    public int fetchMemberIdByNumber(int cardNumber){
        try {
            String sql = "select member_id from cards where number = " + cardNumber;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            if(resultSet.next()){
                return resultSet.getInt("member_id");
            }else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            return -1;
        }
    }
    public String fetchNameByNumber(int cardNumber){
        try {
            String sql = "select name from cards where number = " + cardNumber;
            Statement s = connection.createStatement();
            ResultSet resultSet = s.executeQuery(sql);
            if(resultSet.next()){
                return resultSet.getString("name");
            }else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
