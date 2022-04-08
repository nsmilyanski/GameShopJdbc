package game_shop.entities;

import game_shop.repositories.Identifiable;

import java.math.BigDecimal;
import java.util.Set;

public class Order implements Identifiable<Long> {
    private long id;


    private User buyer;

    private BigDecimal sum;

    public Order() {
    }

    public Order(long id, BigDecimal sum) {
        this.id = id;
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }


    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", sum=" + sum +
                '}';
    }
}
