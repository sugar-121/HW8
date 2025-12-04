import entity.Members;
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
                        int innerChoice = inS.nextInt();
                        if (innerChoice == 2) {
                            flag = false;
                        }
                    }
                    break;
                case 2: {
                    System.out.println("Enter your user name: ");
                    String userName = inS.nextLine();
                    System.out.println("Enter your password: ");
                    int password = inI.nextInt();
                    try {
                        String sql = "select id from members where name = ? and password = ?";
                        PreparedStatement preS = connection.prepareStatement(sql);
                        preS.setString(1, userName);
                        preS.setInt(2, password);
                        ResultSet res = preS.executeQuery();
                        if (res.next()) {
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
                                            int number = random.nextInt(9000) + 1000;
                                            int sheba = random.nextInt(90000) + 10000;
                                            System.out.println("Enter the amount of money you wanna put in: ");
                                            int amount = inI.nextInt();
                                            String sql1 = "insert into cards (name,member_id,number,sheba,amount)" +
                                                    "values (?,?,?,?,?) ";
                                            PreparedStatement preS2 = connection.prepareStatement(sql1);
                                            preS2.setString(1, bankName);
                                            preS2.setInt(2, res.getInt("id"));
                                            preS2.setInt(3, number);
                                            preS2.setInt(4, sheba);
                                            preS2.setInt(5,amount);
                                            preS2.executeUpdate();
                                            break;
                                        }
                                    }
                                }
                            }

                        } else {
                            System.out.println("Wrong user name or password.");
                        }


                    } catch (SQLException e) {
                        throw new RuntimeException(e);
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