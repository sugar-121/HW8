package entity;

import java.sql.Date;
import java.time.LocalDate;


public class Transaction {
    private int srcCard;
    private int destCard;
    private int transAmount;
    private int srcMemberId;
    private int destMemberId;
    private LocalDate date;
    private String situation;

    public int getSrcCard() {
        return srcCard;
    }
    public int getDestCard() {
        return destCard;
    }

    public int getTransAmount() {
        return transAmount;
    }

    public int getSrcMemberId() {
        return srcMemberId;
    }

    public int getDestMemberId() {
        return destMemberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSituation() {
        return situation;
    }

    public Transaction(int srcCard, int destCard, int transAmount, int srcMemberId,
                       int destMemberId, String situation) {
        this.srcCard = srcCard;
        this.destCard = destCard;
        this.transAmount = transAmount;
        this.srcMemberId = srcMemberId;
        this.destMemberId = destMemberId;
        this.date = LocalDate.now();
        this.situation = situation;
    }
}
