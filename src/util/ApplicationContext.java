package util;

import repository.CardsRepository;
import repository.MemberRepository;
import repository.TransactionsRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationContext {
    private static ApplicationContext CTX;
    private Connection connection;
    CardsRepository cardsRepository;
    MemberRepository memberRepository;
    TransactionsRepository transactionsRepository;


    private ApplicationContext() {

    }

    public static ApplicationContext getInstance() {
        if (CTX == null) {
            CTX = new ApplicationContext();
        }
        return CTX;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(ApplicationProperties.DATABASE_URL,
                        ApplicationProperties.DATABASE_USER,
                        ApplicationProperties.DATABASE_PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
    public CardsRepository getCardsRepository(){
        if (cardsRepository == null){
            cardsRepository = new CardsRepository();
        }
        return cardsRepository;
    }
    public MemberRepository getMemberRepository(){
        if (memberRepository == null){
            memberRepository = new MemberRepository();
        }
        return memberRepository;
    }
    public TransactionsRepository getTransactionsRepository(){
        if (transactionsRepository == null){
            transactionsRepository = new TransactionsRepository();
        }
        return transactionsRepository;
    }

}
