import entity.Cards;
import entity.Members;
import repository.CardsRepository;
import repository.MemberRepository;
import repository.TransactionsRepository;
import util.ApplicationContext;

import java.util.Scanner;

public class Menu {
        Scanner in = new Scanner(System.in);
        MemberRepository memberRepository = ApplicationContext.getInstance().getMemberRepository();
        CardsRepository cardsRepository = ApplicationContext.getInstance().getCardsRepository();
        TransactionsRepository transactionsRepository = ApplicationContext.getInstance().getTransactionsRepository();

        public void start() {
            while (true) {
                System.out.println("""
                    1. Sign up
                    2. Log in
                    3. Exit
                    """);

                int choice = in.nextInt();
                in.nextLine(); // clear buffer

                switch (choice) {
                    case 1 -> handleSignup();
                    case 2 -> handleLogin();
                    case 3 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid input.");
                }
            }
        }

        private void handleSignup() {
            while (true) {
                System.out.println("Enter username: ");
                String username = in.nextLine();
                System.out.println("Enter password: ");
                int password = in.nextInt();
                in.nextLine();

                Members m = new Members(username, password);
                if (memberRepository.regMember(m)) {
                    System.out.println("Signup successful.");
                    return;
                }
                System.out.println("Username is taken. Try another.");
            }
        }

        private void handleLogin() {
            while (true) {
                System.out.println("Enter username: ");
                String username = in.nextLine();
                System.out.println("Enter password: ");
                int password = in.nextInt();
                in.nextLine();

                Members m = new Members(username, password);
                int id = memberRepository.fetchMember(m);

                if (id != -1) {
                    System.out.println("Logged in successfully!");
                    showUserMenu(id);
                    return;
                }
                System.out.println("Wrong username or password. Try again.");
            }
        }

        private void showUserMenu(int memberId) {
            while (true) {
                System.out.println("""
                    1. Card services
                    2. Financial services
                    3. Back
                    """);

                int choice = in.nextInt();
                in.nextLine();

                switch (choice) {
                    case 1 -> showCardMenu(memberId);
                    case 2 -> showFinancialMenu(memberId);
                    case 3 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }

        private void showCardMenu(int memberId) {
            while (true) {
                System.out.println("""
                    1. Register card
                    2. Delete card
                    3. Show card by number
                    4. Show cards by bank name
                    5. Show all cards
                    6. Back
                    """);

                int choice = in.nextInt();
                in.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.println("Enter bank name: ");
                        String bank = in.nextLine();
                        System.out.println("Enter initial amount: ");
                        int amount = in.nextInt();
                        in.nextLine();

                        cardsRepository.insertCard(new Cards(bank, amount), memberId);
                    }
                    case 2 -> {
                        System.out.println("Enter bank name: ");
                        String bank = in.nextLine();
                        cardsRepository.showCardByName(bank, memberId);
                        System.out.println("Enter card ID to delete: ");
                        int id = in.nextInt();
                        in.nextLine();

                        cardsRepository.deleteCardById(id);
                    }
                    case 3 -> {
                        System.out.println("Enter card number: ");
                        int num = in.nextInt();
                        in.nextLine();
                        cardsRepository.showCardByNumber(num, memberId);
                    }
                    case 4 -> {
                        System.out.println("Enter bank name: ");
                        String bank = in.nextLine();
                        cardsRepository.showCardByName(bank, memberId);
                    }
                    case 5 -> cardsRepository.showAllCards(memberId);
                    case 6 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }

        private void showFinancialMenu(int memberId) {
            while (true) {
                System.out.println("""
                    1. Card to card
                    2. Paya transfer (single)
                    3. Back
                    """);

                int choice = in.nextInt();
                in.nextLine();

                switch (choice) {
                    case 1 -> handleCardToCard();
                    case 2 -> handlePaya();
                    case 3 -> { return; }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }

        private void handleCardToCard() {
            System.out.println("Enter source card: ");
            int src = in.nextInt();
            System.out.println("Enter destination card: ");
            int dest = in.nextInt();
            System.out.println("Enter amount: ");
            int amount = in.nextInt();
            in.nextLine();

            try {
                transactionsRepository.cardToCardTransaction(src, dest, amount);
            } catch (Exception e) {
                System.out.println("Transaction failed.");
            }
        }

        private void handlePaya() {
            System.out.println("Enter source card: ");
            int src = in.nextInt();
            System.out.println("Enter destination card: ");
            int dest = in.nextInt();
            System.out.println("Enter amount: ");
            int amount = in.nextInt();
            in.nextLine();

            transactionsRepository.insertIntoPayaTransactions(src, dest, amount);
        }
    }


