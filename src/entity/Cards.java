package entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Cards {
    private String name;
    private Integer number;
    private Integer sheba;
    private Integer amount;
    Random random = new Random();
    private Set<Integer> cardNumbers = new HashSet<>();
    private Set<Integer> cardShebas = new HashSet<>();

    public Cards(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
        while (true){
            int n = random.nextInt(9000) + 1000;
            if(cardNumbers.add(n)){
                this.number = n;
                break;
            }
        }
        while (true){
            int m = random.nextInt(90000) + 10000;
            if(cardShebas.add(m)){
                this.sheba = m;
                break;
            }
        }
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getSheba() {
        return sheba;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
