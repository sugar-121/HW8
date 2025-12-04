import entity.Cards;
import entity.Members;
import repository.CardsRepository;
import repository.MemberRepository;
import util.ApplicationContext;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.getInstance();
        Connection connection = ctx.getConnection();
        MemberRepository memberRepository = new MemberRepository();
        CardsRepository cardsRepository = new CardsRepository();
        Scanner inS = new Scanner(System.in);
        Scanner inI = new Scanner(System.in);
        Random random = new Random();
        boolean flag = true;
        while (flag) {
            System.out.println("""
                    1. sign up
                    2. log in 
                    3. exit
                    """);
            int choice = inI.nextInt();

            switch (choice) {
                case 1: {
                    while (true){
                        System.out.println("Please enter a user name: ");
                        String userName = inS.nextLine();
                        System.out.println("Please enter a password: ");
                        int password = inI.nextInt();
                        Members member = new Members(userName, password);
                        boolean b = memberRepository.regMember(member);
                        if (!b){
                            System.out.println("Sorry! your user name is taken. enter another one.");
                        }else {
                            break;
                        }
                    }
                    System.out.println("You signed up successfully.");
                        System.out.println("""
                                1. main menu
                                2. exit
                                """);
                        if (inI.nextInt() == 2) {
                            flag = false;
                        }
                    }
                    break;
                case 2: {
                    while (true){
                        System.out.println("Enter your user name: ");
                        String userName = inS.nextLine();
                        System.out.println("Enter your password: ");
                        int password = inI.nextInt();
                        Members member = new Members(userName,password);
                        int memberId = memberRepository.fetchMember(member);
                        if (memberId != -1){
                            System.out.println("You are in!! ");
                            System.out.println("Choose your service: ");
                            System.out.println("""
                                    1. card services
                                    2. financial services
                                    """);
                            switch (inI.nextInt()) {
                                case 1: {
                                    System.out.println("""
                                            1. card registration
                                            """);
                                    switch (inI.nextInt()) {
                                        case 1: {
                                            System.out.println("Enter the bank name: ");
                                            String bankName = inS.nextLine();
                                            System.out.println("Enter the amount of money you wanna put in: ");
                                            int amount = inI.nextInt();
                                            Cards card = new Cards(bankName , amount);
                                            cardsRepository.insertCard(card,memberId);
                                            break;
                                        }
                                    }
                                }
                            }
                        }else {
                            System.out.println("Wrong user name or password. Try again. ");
                        }
                    }
                }
            }
        }


//
//        try {
//            connection.setAutoCommit(false);
//            Statement statement = connection.createStatement();
//            statement.execute("drop table users");
//            statement.close();
//            connection.commit();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


    }
}