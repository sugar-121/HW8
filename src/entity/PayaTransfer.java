package entity;

import java.time.LocalDate;

public class PayaTransfer {
    private int srcCard;
    private int destCard;
    private int transAmount;
    private LocalDate date;

    public PayaTransfer(int srcCard, int destCard, int transAmount, LocalDate date) {
        this.srcCard = srcCard;
        this.destCard = destCard;
        this.transAmount = transAmount;
        this.date = LocalDate.now();
    }

    public int getSrcCard() {
        return srcCard;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTransAmount() {
        return transAmount;
    }

    public int getDestCard() {
        return destCard;
    }
}
