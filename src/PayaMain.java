import repository.TransactionsRepository;
import util.ApplicationContext;

public class PayaMain {
    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.getInstance();
        TransactionsRepository transactionsRepository = ctx.getTransactionsRepository();
        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println(transactionsRepository.payaChecker());
                System.out.println(transactionsRepository.payaDeletion());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
