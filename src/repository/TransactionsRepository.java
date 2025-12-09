package repository;

import entity.PayaTransfer;
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
        if (amount > 15000000) {
            System.out.println("More than the limit. ");
            return false;
        }
        int amountS, amountD, fee;
        amountS = amountD = amount;
        fee = 0;
        int srcAmount = cardsRepository.fetchCardAmountByNumber(srcCardNumber);
        if (srcAmount == -1) {
            System.out.println("Source card doesnt exist.");
            return false;
        }
        int srcId = cardsRepository.fetchCardIdByNumber(srcCardNumber);
        try {
            int destAmount = cardsRepository.fetchCardAmountByNumber(destCardNumber);
            if (destAmount == -1) {
                System.out.println("destination card doesnt exist.");
                return false;
            }
            int destId = cardsRepository.fetchCardIdByNumber(destCardNumber);
            String srcBankName = cardsRepository.fetchNameByNumber(srcCardNumber);
            String destBankName = cardsRepository.fetchNameByNumber(destCardNumber);
            if (!srcBankName.equals(destBankName)) {
                if (amount <= 10000000) {
                    fee = 720;
                    amountS = amount + fee;
                } else {
                    int extra = (amount - 10000000) / 1000000;
                    fee = 1000 + (extra * 100);
                    amountS = amount + fee;

                }
            }
            if (amountS > srcAmount) {
                insertIntoTransactions(srcCardNumber, destCardNumber, amountS, "unsuccessful");
                System.out.println("Dont have enough balance! ");
                return false;
            }
            connection.setAutoCommit(false);
            String sql1 = "update cards set amount = " + (srcAmount - amountS) + " where id = " + srcId;
            Statement s1 = connection.createStatement();
            s1.executeUpdate(sql1);
            String sql2 = "update cards set amount = " + (destAmount + amountD) + " where id = " + destId;
            Statement s2 = connection.createStatement();
            s2.executeUpdate(sql2);
            insertIntoTransactions(srcCardNumber, destCardNumber, amountS, "successful");
            connection.commit();
            System.out.println("Transaction is done successfully.");
            System.out.println("Fee for this transaction: " + fee);
            s1.close();
            s2.close();
            return true;
        } catch (SQLException e) {
            System.out.println("some thing went wrong. Rolling back.");
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("can not roll back");
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                insertIntoTransactions(srcCardNumber, destCardNumber, amountS, "unsuccessful");
                connection.close();
            }
            return false;
        }
    }

    public void insertIntoPayaTransactions(int srcCardNumber, int destCardNumber, int amount) {
        try {
            String sql = "insert into paya_transfer (id ,src_card, dest_card, amount)\n" +
                    "VALUES (?,?,?,?);";
            PreparedStatement preS = connection.prepareStatement(sql);
            preS.setInt(1,1);
            preS.setInt(2, srcCardNumber);
            preS.setInt(3, destCardNumber);
            preS.setInt(4, amount);
            preS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean payaChecker() {
        try {

            String sql = "select * from paya_transfer ";
            PreparedStatement preS = connection.prepareStatement(sql);
            ResultSet resultSet = preS.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            int srcCard = resultSet.getInt("src_card");
            int destCard = resultSet.getInt("dest_card");
            int amount = resultSet.getInt("amount");
            if (amount > 50000000) {
                System.out.println("More than the limit. ");
                return false;
            }
            int amountS, amountD, fee;
            amountS = amountD = amount;
            fee = 0;
            int srcAmount = cardsRepository.fetchCardAmountByNumber(srcCard);
            if (srcAmount == -1) {
                System.out.println("Source card doesnt exist.");
                return false;
            }
            int srcId = cardsRepository.fetchCardIdByNumber(srcCard);

            int destAmount = cardsRepository.fetchCardAmountByNumber(destCard);
            if (destAmount == -1) {
                System.out.println("destination card doesnt exist.");
                return false;
            }
            int destId = cardsRepository.fetchCardIdByNumber(destCard);
            String srcBankName = cardsRepository.fetchNameByNumber(srcCard);
            String destBankName = cardsRepository.fetchNameByNumber(destCard);
            if (!srcBankName.equals(destBankName)) {
                if (amount <= 10000000) {
                    fee = 720;
                    amountS = amount + fee;
                } else {
                    int extra = (amount - 10000000) / 1000000;
                    fee = 1000 + (extra * 100);
                    amountS = amount + fee;

                }
            }
            if (amountS > srcAmount) {
                insertIntoTransactions(srcCard, destCard, amountS, "unsuccessful");
                System.out.println("Dont have enough balance! ");
                return false;
            }
            String sql1 = "update cards set amount = " + (srcAmount - amountS) + " where id = " + srcId;
            Statement s1 = connection.createStatement();
            s1.executeUpdate(sql1);
            String sql2 = "update cards set amount = " + (destAmount + amountD) + " where id = " + destId;
            Statement s2 = connection.createStatement();
            s2.executeUpdate(sql2);
            System.out.println("Transaction is done successfully.");
            System.out.println("Fee for this transaction: " + fee);
            insertIntoTransactions(srcCard, destCard, amountS, "successful");
            return true;
        } catch (SQLException e) {
            System.out.println("some thing went wrong.");
        }
        return false;
    }

    public boolean payaDeletion() {
        try {
            String sql = "delete from paya_transfer where id = 1";
            PreparedStatement preS = connection.prepareStatement(sql);
            ResultSet resultSet = preS.executeQuery();
        } catch (SQLException e) {
           return false;
        }
        return true;
    }


}
