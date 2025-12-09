package repository;

import entity.Transaction;
import util.ApplicationContext;

import java.sql.*;

public class TransactionsRepository {
    Connection connection = ApplicationContext.getInstance().getConnection();
    CardsRepository cardsRepository = new CardsRepository();

    public void insertIntoTransactions(int srcCardNumber, int destCardNumber, int amount, String situation) {
        int srcMemberId = cardsRepository.fetchMemberIdByNumber(srcCardNumber);
        int destMemberId = cardsRepository.fetchMemberIdByNumber(destCardNumber);
        Transaction transaction = new Transaction(srcCardNumber, destCardNumber, amount, srcMemberId, destMemberId, situation);
        try {
            String sql = "insert into transactions (src_member_id, dest_member_id, src_card, dest_card, amount, situation)\n" +
                    "VALUES (?,?,?,?,?,?);";
            PreparedStatement preS = connection.prepareStatement(sql);
            preS.setInt(1, transaction.getSrcMemberId());
            preS.setInt(2, transaction.getDestMemberId());
            preS.setInt(3, transaction.getSrcCard());
            preS.setInt(4, transaction.getDestCard());
            preS.setInt(5, transaction.getTransAmount());
            preS.setString(6, transaction.getSituation());
            preS.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insert");
            throw new RuntimeException(e);
        }
    }


    public boolean cardToCardTransaction(int srcCardNumber, int destCardNumber, int amount) throws SQLException {
        int srcAmount = cardsRepository.fetchCardAmountByNumber(srcCardNumber);
        if (srcAmount == -1) {
            System.out.println("Source card doesnt exist.");
            return false;
        }
        int srcId = cardsRepository.fetCardIdByNumber(srcCardNumber);
        if (amount > srcAmount) {
            System.out.println("Dont have enough balance! ");
            return false;
        }
        try {
            int destAmount = cardsRepository.fetchCardAmountByNumber(destCardNumber);
            if (destAmount == -1) {
                System.out.println("destination card doesnt exist.");
                return false;
            }
            int destId = cardsRepository.fetCardIdByNumber(destCardNumber);
            connection.setAutoCommit(false);
            String sql1 = "update cards set amount = " + (srcAmount - amount) + " where id = " + srcId;
            Statement s1 = connection.createStatement();
            s1.executeUpdate(sql1);
            String sql2 = "update cards set amount = " + (destAmount + amount) + " where id = " + destId;
            Statement s2 = connection.createStatement();
            s2.executeUpdate(sql2);
            insertIntoTransactions(srcCardNumber, destCardNumber, amount, "successful");
            connection.commit();
            System.out.println("Transaction is done successfully.");
            s1.close();
            s2.close();
            return true;
        } catch (SQLException e) {
            System.out.println("some thing went wrong. Rolling back.");
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("can not roll back");
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                insertIntoTransactions(srcCardNumber, destCardNumber, amount, "unsuccessful");
                connection.close();
            }
            return false;
        }
    }
}
